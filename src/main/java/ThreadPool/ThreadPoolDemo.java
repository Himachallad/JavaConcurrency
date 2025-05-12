package ThreadPool;

public class ThreadPoolDemo {
  public static void main(String[] args) {
    System.out.println("Thread Pool Demo");
    ThreadPool threadPool = ThreadPool.getInstance(3);

    // Submitting tasks to ThreadPool
    for (int i = 1; i <= 10; i++) {
      Task task = new Task("Task-" + i);
      threadPool.submitTask(task);
    }

    // Shutting down the thread pool
//    threadPool.shutdown();
  }
}
