package ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPool {
  private static ThreadPool instance;
  private final BlockingQueue<Runnable> taskQueue;
  private final Thread[] workerThreads;
  private final AtomicBoolean isShutdownInitiated;
  private ThreadPool(int numOfThreads) {
    taskQueue = new LinkedBlockingDeque<>();
    workerThreads = new Thread[numOfThreads];
    isShutdownInitiated = new AtomicBoolean(false);

    for (int i = 0; i < numOfThreads; i++) {
      workerThreads[i] = new Worker(taskQueue, isShutdownInitiated);
      workerThreads[i].start();
    }
  }


  public static synchronized ThreadPool getInstance(int numberOfThreads) {
    if (instance == null) {
      instance = new ThreadPool(numberOfThreads);
    }
    return instance;
  }

  public void submitTask(Runnable task) {
    if (!isShutdownInitiated.get()) {
      taskQueue.offer(task);
    }
  }

  public void shutdown() {
    isShutdownInitiated.set(true);
    for (Thread worker : workerThreads) {
      worker.interrupt();
    }
  }
}
