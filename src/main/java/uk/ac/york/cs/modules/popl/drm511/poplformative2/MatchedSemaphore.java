package uk.ac.york.cs.modules.popl.drm511.poplformative2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Wraps the native Java Semaphore to guarantee matched interactions
 *
 * It is an error for a client to release a different number of permits from
 * this semaphore from the number the client acquired.
 *
 * Clients are identified by passing their 'this' pointer 
 * to acquire() and release()
 *
 * @author drm
 */
public class MatchedSemaphore {

    private Semaphore semaphore;
    private HashMap<Object, ArrayList<Integer>> clients;

    public MatchedSemaphore(int permits) {
        semaphore = new Semaphore(permits);
        clients = new HashMap<Object, ArrayList<Integer>>();
    }

    public void acquire(Object client, int permits) {
        semaphore.acquireUninterruptibly(permits);
        synchronized (this) {
            allocatePermitsToClient(client, permits);
        }
    }

    public synchronized void release(Object client, int permits) {
        clientMustExist(client);
        clientMustHavePermits(client, permits);
        semaphore.release(permits);
        deallocatePermitsFromClient(client, permits);
        removeClientIfEmpty(client);
    }

    private boolean allocatePermitsToClient(Object client, int permits) {
        ensureClientHasEntry(client);
        return clients.get(client).add(permits);
    }

    private void clientMustExist(Object client) throws UnmatchedAccessException {
        if (!hasClient(client)) {
            throw new UnmatchedAccessException();
        }
    }

    private void clientMustHavePermits(Object client, int permits) throws UnmatchedAccessException {
        if (!clientHasNPermits(client, permits)) {
            throw new UnmatchedAccessException();
        }
    }

    private void deallocatePermitsFromClient(Object client, int permits) {
        clients.get(client).remove(new Integer(permits));
    }

    private void removeClientIfEmpty(Object client) {
        if (clientHasNoPermits(client)) {
            clients.remove(client);
        }
    }

    private void ensureClientHasEntry(Object client) {
        if (!hasClient(client)) {
            clients.put(client, new ArrayList<Integer>());
        }
    }

    private boolean hasClient(Object client) {
        return clients.containsKey(client);
    }

    private boolean clientHasNPermits(Object client, int permits) {
        return clients.get(client).contains(permits);
    }

    private boolean clientHasNoPermits(Object client) {
        return clients.get(client).isEmpty();
    }
}
