import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {
    private int id;
    private Object leftFork;
    private Object rightFork;
    private ReentrantLock lock;

    // Tracking statistics to print at termination
    private int eatCount = 0;
    private long totalEatTime = 0;
    private long totalWaitTime = 0;

    public Philosopher(int id, Object leftFork, Object rightFork, ReentrantLock lock) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.lock = lock;
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

                // Measure wait time before eating
                long waitStartTime = System.currentTimeMillis();

                // Use a reentrant lock as a mutex so that only one thread can access critical code at a time
                lock.lock();
                try {
                    // Even-numbered philosophers pick up the left fork first, odd-numbered the right
                    // Prevents deadlock because all of them will never pick up the same fork at the same time
                    if (id % 2 == 0) {
                        synchronized (leftFork) {
                            synchronized (rightFork) {
                                // Wait time ends here before eating
                                long waitEndTime = System.currentTimeMillis();
                                totalWaitTime += (waitEndTime - waitStartTime);
                                eat();
                            }
                        }
                    } else {
                        synchronized (rightFork) {
                            synchronized (leftFork) {
                                // Wait time ends here before eating
                                long waitEndTime = System.currentTimeMillis();
                                totalWaitTime += (waitEndTime - waitStartTime);
                                eat();
                            }
                        }
                    }
                } finally {
                    // Release the lock after eating, so other philosophers can pick up the forks
                    lock.unlock();
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
