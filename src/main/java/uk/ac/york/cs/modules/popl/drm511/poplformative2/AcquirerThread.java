/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.york.cs.modules.popl.drm511.poplformative2;
import java.util.concurrent.Semaphore;


/**
 *
 * @author drm
 */
public class AcquirerThread extends Thread {
    
    private Semaphore semaphore;
    private int quantity;

    public AcquirerThread(Semaphore semaphore, int quantity) {
        this.semaphore = semaphore;
        this.quantity = quantity;
    }
    
    @Override
    public void run() {
        System.out.println("Starting AcquirerThread for quantity = " + quantity);
        for (int i = 0; i < 5; i++) {
            System.out.println("Acquiring " + quantity + " permits...");
            semaphore.acquireUninterruptibly(quantity);
            System.out.println("Acquired " + quantity + " permits.");
            semaphore.release(quantity);
            System.out.println("Released " + quantity + " permits.");
        }
        System.out.println("Finished AcquirerThread for quantity = " + quantity);
    }
    
}
