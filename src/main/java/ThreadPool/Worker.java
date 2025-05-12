package ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker extends Thread {
  private final BlockingQueue<Runnable> taskQueue;
  private final AtomicBoolean isShutdownInitiated;

  public Worker(BlockingQueue<Runnable> taskQueue, AtomicBoolean isShutdownInitiated) {
    this.taskQueue = taskQueue;
    this.isShutdownInitiated = isShutdownInitiated;
  }

  @Override
  public void run() {
    while(!isShutdownInitiated.get() || !taskQueue.isEmpty()) {
      try {
        Runnable task = taskQueue.take();
        task.run();
      } catch (InterruptedException e) {
        if (isShutdownInitiated.get()) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    }
  }
}
