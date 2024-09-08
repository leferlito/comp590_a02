import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
  public static int numberOfPhilosophers = 5;
  public static Semaphore semaphore = new Semaphore(numberOfPhilosophers - 1);
  public static ReentrantLock lock = new ReentrantLock(true);

  public static void main(String[] args) {
    Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
    Object[] forks = new Object[numberOfPhilosophers];

    // Create fork objects
    for (int i = 0; i < numberOfPhilosophers; i++) {
      forks[i] = new Object();
    }

    // Create philosopher threads
    for (int i = 0; i < numberOfPhilosophers; i++) {
      Object leftFork = forks[i];
      Object rightFork = forks[(i + 1) % numberOfPhilosophers];

      philosophers[i] = new Philosopher(i, leftFork, rightFork, semaphore, lock);
      philosophers[i].start();
    }

    // Shutdown hook to collect and print data when program is terminated
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("\nProgram terminated. Printing statistics...");
      for (Philosopher philosopher : philosophers) {
        philosopher.printStatistics();
      }
    }));
  }
}
