package ThreadPool;

public class Task implements Runnable {
  private final String taskName;

  public Task(String taskName) {
    this.taskName = taskName;
  }

  @Override
  public void run() {
    System.out.println("Executing Task: " + taskName);
    try {
      System.out.println("Waiting for " + taskName);
      Thread.sleep(2000); // Simulate task execution
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    System.out.println("Task Completed: " + taskName);
  }
}
