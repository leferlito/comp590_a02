// Lauren Ferlito and AJ Valentino

import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private int id;
    private Object leftFork;
    private Object rightFork;
    private Semaphore semaphore;

    // Tracking statistics to print at termination
    private int eatCount = 0;
    private long totalEatTime = 0;
    private long totalWaitTime = 0;

    public Philosopher(int id, Object leftFork, Object rightFork, Semaphore semaphore) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.semaphore = semaphore;
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking.");
        Thread.sleep((int)(Math.random() * 100));  // Random think time helps implement fairness
    }

    private void eat() throws InterruptedException {
        long startEatTime = System.currentTimeMillis();  // Start logging eating time

        System.out.println("Philosopher " + id + " is eating.");
        Thread.sleep((int)(Math.random() * 100));

        long endEatTime = System.currentTimeMillis();  // End logging eating time

        // Update eat count and total eating time
        eatCount++;
        totalEatTime += (endEatTime - startEatTime);
    }

    public void run() {
        try {
            while (true) {
                think();

                // Measure wait time
                long waitStartTime = System.currentTimeMillis();

                // Semaphore limits the number of philosophers trying to pick up forks to n-1 to prevent deadlock aid fairness
                semaphore.acquire();
                try {
                    // Even-numbered philosophers pick up the left fork first, odd-numbered the right
                    // Prevents deadlock because all of them will never pick up the same side of fork at the same time
                    if (id % 2 == 0) {
                        synchronized (leftFork) {
                            synchronized (rightFork) {
                                // Wait time ends here before eating and calculate total wait time
                                long waitEndTime = System.currentTimeMillis();
                                totalWaitTime += (waitEndTime - waitStartTime);
                                eat();
                            }
                        }
                    } else {
                        synchronized (rightFork) {
                            synchronized (leftFork) {
                                // Wait time ends here before eating and calculate total wait time
                                long waitEndTime = System.currentTimeMillis();
                                totalWaitTime += (waitEndTime - waitStartTime);
                                eat();
                            }
                        }
                    }
                } finally {
                    // Release the semaphore after eating, so new philosophers can pick up the forks
                    semaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Prints out stats once program is terminated
    public void printStatistics() {
        System.out.println("Philosopher " + id + " ate " + eatCount + " times.");
        System.out.println("Philosopher " + id + " spent a total of " + totalEatTime + " ms eating.");
        System.out.println("Philosopher " + id + " waited a total of " + totalWaitTime + " ms waiting to eat.");
    }
}
