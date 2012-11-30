package uk.ac.york.cs.modules.popl.drm511.poplformative2;

/**
 * Example client for a MatchedSemaphore
 * @author drm
 */
public class AcquirerThread extends Thread {

    private MatchedSemaphore semaphore;
    private int quantity;

    public AcquirerThread(MatchedSemaphore semaphore, int quantity) {
        this.semaphore = semaphore;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        System.out.println("Starting AcquirerThread for quantity = " + quantity);
        for (int i = 0; i < 5; i++) {
            System.out.println("Acquiring " + quantity + " permits on iteration " + i + "...");
            semaphore.acquire(quantity);
            System.out.println("Acquired " + quantity + " permits on iteration " + i + ".");
            semaphore.release(quantity);
            System.out.println("Released " + quantity + " permits on iteration " + i + ".");
        }
        System.out.println("Finished AcquirerThread for quantity = " + quantity);
    }
}
