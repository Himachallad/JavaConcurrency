package TaskSchedularImplementation.main.helper;

import TaskSchedularImplementation.main.entity.Job;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class JobExecutor implements Runnable {
  private final Executor jobExecutors;
  private final Queue<Job> jobPriorityQueue;
  private final Lock queueLock;
  private final Condition entryAdded;
  private final int threadCount;
  public JobExecutor(Queue<Job> jobPriorityQueue, Lock queueLock, Condition entryAdded, int threadCount) {
    this.jobPriorityQueue = jobPriorityQueue;
    this.queueLock = queueLock;
    this.entryAdded = entryAdded;
    this.threadCount = threadCount;
    jobExecutors = Executors.newFixedThreadPool(threadCount);
  }

  @Override
  public void run() {
    while(true) {
      queueLock.lock();
      try {
        if (!jobPriorityQueue.isEmpty()) {
          Job job = jobPriorityQueue.peek();
          Date startTime = job.getStartTime();
          Date current = Calendar.getInstance().getTime();

          if (current.compareTo(startTime) >= 0) {
            jobPriorityQueue.remove();
            jobExecutors.execute(job);
          }
        } else {
          try {
            entryAdded.await();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      } finally {
        queueLock.unlock();
      }
    }
  }
}
