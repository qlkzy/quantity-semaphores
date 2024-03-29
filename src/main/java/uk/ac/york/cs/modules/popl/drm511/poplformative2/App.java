package uk.ac.york.cs.modules.popl.drm511.poplformative2;

import java.util.concurrent.Semaphore;

/**
 * Demonstrate MatchedSemaphore
 */
public class App 
{
    public static void main( String[] args )
    {
        MatchedSemaphore semaphore = new MatchedSemaphore(10);
        int[] counts = {1, 2, 4, 6, 10};
        AcquirerThread[] threads = new AcquirerThread[counts.length];
        for (int i = 0; i < counts.length; i++) {
            threads[i] = new AcquirerThread(semaphore, counts[i]);
        }
        for (AcquirerThread t : threads) {
            t.start();
        }
    }
}
