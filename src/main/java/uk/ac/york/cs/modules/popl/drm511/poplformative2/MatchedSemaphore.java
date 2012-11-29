/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.york.cs.modules.popl.drm511.poplformative2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
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
            if (!clients.containsKey(client)) {
                clients.put(client, new ArrayList<Integer>());
            }
            clients.get(client).add(permits);
        }
    }

    public void release(Object client, int permits) {
        synchronized (this) {
            // we must have given permits to that client
            if (!clients.containsKey(client)) {
                throw new UnmatchedAccessException();
            }
            // we must have given that many permits to that client
            if (!clients.get(client).contains(permits)) {
                throw new UnmatchedAccessException();
            }
            // release the permits both from the semaphore and our store
            semaphore.release(permits);
            clients.get(client).remove(new Integer(permits));
            // don't bother keeping information for old clients we no longer care about
            if (clients.get(client).isEmpty()) {
                clients.remove(client);
            }
        }
    }
}
