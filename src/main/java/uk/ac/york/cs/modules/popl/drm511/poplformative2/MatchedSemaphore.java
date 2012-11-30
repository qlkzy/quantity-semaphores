package uk.ac.york.cs.modules.popl.drm511.poplformative2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Wraps the native Java Semaphore to guarantee matched interactions
 *
 * It is an error for a thread to release a different number of permits from
 * this semaphore from the number the thread acquired.
 *
 * Threads are identified by passing their 'this' pointer to acquire() and
 * release()
 *
 * @author drm
 */
public class MatchedSemaphore {

    private Semaphore semaphore;
    private HashMap<Object, ArrayList<Integer>> threads;

    public MatchedSemaphore(int permits) {
        semaphore = new Semaphore(permits);
        threads = new HashMap<Object, ArrayList<Integer>>();
    }

    public void acquire(int permits) {
        semaphore.acquireUninterruptibly(permits);
        allocatePermits(permits);
    }

    public synchronized void release(int permits) {
        threadMustExist();
        threadMustHavePermits(permits);
        semaphore.release(permits);
        deallocatePermits(permits);
        removeThreadIfEmpty();
    }

    private synchronized boolean allocatePermits(int permits) {
        ensureThreadHasEntry();
        return threadEntry().add(permits);
    }

    private void deallocatePermits(int permits) {
        threadEntry().remove(new Integer(permits));
    }

    private void threadMustExist() {
        if (!hasThreadEntry()) {
            throw new UnmatchedAccessException();
        }
    }

    private void threadMustHavePermits(int permits) {
        if (!threadHas_N_Permits(permits)) {
            throw new UnmatchedAccessException();
        }
    }

    private void removeThreadIfEmpty() {
        if (threadHasNoPermits()) {
            threads.remove(currentThread());
        }
    }

    private void ensureThreadHasEntry() {
        if (!hasThreadEntry()) {
            threads.put(currentThread(), new ArrayList<Integer>());
        }
    }

    private boolean threadHas_N_Permits(int permits) {
        return threadEntry().contains(permits);
    }

    private boolean threadHasNoPermits() {
        return threadEntry().isEmpty();
    }

    private boolean hasThreadEntry() {
        return threads.containsKey(currentThread());
    }

    private ArrayList<Integer> threadEntry() {
        return threads.get(currentThread());
    }

    private Thread currentThread() {
        return Thread.currentThread();
    }
}
