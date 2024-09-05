public class Main {
  public static int numberOfPhilosophers = 5;
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

      // Instantiate the philosopher objects and start the threads
      philosophers[i] = new Philosopher(i, leftFork, rightFork);
      philosophers[i].start();
    }
  }
}
