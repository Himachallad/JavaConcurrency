package TaskSchedularImplementation.main.service;

import TaskSchedularImplementation.main.entity.Job;
import TaskSchedularImplementation.main.enums.TaskType;
import TaskSchedularImplementation.main.helper.JobExecutor;
import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SchedulerServiceImpl implements SchedulerService {
  private static final SchedulerService INSTANCE = new SchedulerServiceImpl(Runtime.getRuntime().availableProcessors() - 1);
  private final Queue<Job> jobPriorityQueue;
  private final Lock queueLock;
  private final Condition entryAdded;
  private SchedulerServiceImpl(int threadCount) {
    jobPriorityQueue = new PriorityQueue<>();
    queueLock = new ReentrantLock();
    entryAdded = queueLock.newCondition();

    Thread executor = new Thread(new JobExecutor(jobPriorityQueue, queueLock, entryAdded, threadCount));
    executor.start();
  }

  public static SchedulerService getInstance() {
    return INSTANCE;
  }
  private void addJobToQueue(Job job) {
    queueLock.lock();
    try {
      jobPriorityQueue.offer(job);
      entryAdded.signal();
    } finally {
      queueLock.unlock();
    }
  }
  @Override
  public void schedule(Runnable task, long initialDelay, TimeUnit timeUnit) {
    Date date = new Date(Calendar.getInstance().getTime().getTime() + timeUnit.toMillis(initialDelay));
    Job job = new Job(UUID.randomUUID().hashCode(), task, date);

    addJobToQueue(job);
  }


  @Override
  public void scheduleWithFixedDelay(Runnable task, long initialDelay, long fixedDelay, TimeUnit timeUnit) {
    Date date = new Date(Calendar.getInstance().getTime().getTime() + timeUnit.toMillis(initialDelay));
    Job job = new Job(UUID.randomUUID().hashCode(), task, date, fixedDelay, TimeUnit.SECONDS, TaskType.REPEAT_FIXED_DELAY);

    addJobToQueue(job);
  }

  @Override
  public void scheduleWithFixedRate(Runnable task, long initialDelay, long fixedRate, TimeUnit timeUnit) {
    Date date = new Date(Calendar.getInstance().getTime().getTime() + timeUnit.toMillis(initialDelay));
    Job job = new Job(UUID.randomUUID().hashCode(), task, date, fixedRate, TimeUnit.SECONDS, TaskType.REPEAT_FIXED_RATE);

    addJobToQueue(job);
  }
}
