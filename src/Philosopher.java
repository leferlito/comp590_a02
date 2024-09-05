import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private int id;
    private Object leftFork;
    private Object rightFork;

    public Philosopher(int id, Object leftFork, Object rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking.");
        Thread.sleep((int)(Math.random() * 100));
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating.");
        Thread.sleep((int)(Math.random() * 100));
    }

    // Asymmetrical approach: even numbered philosophers take left first, odd numbered take right first
    public void run() {
        try {
            while (true) {
                think();

                if (id % 2 == 0) {
                    synchronized (leftFork) {
                        synchronized (rightFork) {
                            eat();
                        }
                    }
                } else {
                    synchronized (rightFork) {
                        synchronized (leftFork) {
                            eat();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
