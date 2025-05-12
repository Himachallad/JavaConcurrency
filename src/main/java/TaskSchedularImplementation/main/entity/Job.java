package TaskSchedularImplementation.main.entity;

import TaskSchedularImplementation.main.enums.TaskType;
import TaskSchedularImplementation.main.service.SchedulerService;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Job implements Comparable<Job>, Runnable {
  private final int jobId;
  private final Runnable task;
  private final Date startTime;
  private final long reschedulePeriod;
  private final TimeUnit unit;
  private final TaskType taskType;

  public Job(int jobId, Runnable task, Date startTime) {
    this(jobId, task, startTime, -1, TimeUnit.SECONDS, TaskType.ONCE);
  }

  public Job(int jobId, Runnable task, Date startTime, long reschedulePeriod, TimeUnit unit, TaskType taskType) {
    this.jobId = jobId;
    this.task = task;
    this.startTime = startTime;
    this.reschedulePeriod = reschedulePeriod;
    this.unit = unit;
    this.taskType = taskType;
  }

  public Date getStartTime() {
    return startTime;
  }

  @Override
  public int compareTo(Job o) {
    long comparedValue = this.startTime.getTime() - o.startTime.getTime();
    return (comparedValue> 0) ? -1 : (comparedValue == 0 ? 0: -1);
  }

  @Override
  public void run() {
    if(TaskType.REPEAT_FIXED_RATE.equals(this.taskType)) {
      SchedulerService.getInstance().scheduleWithFixedRate(this.task, this.reschedulePeriod, reschedulePeriod, unit);
    }
    System.out.printf("[%s] Running main.entity.job : %s at time %d%n", taskType, jobId, Calendar.getInstance().get(Calendar.SECOND));
    task.run(); // sync-wait

    if(TaskType.REPEAT_FIXED_DELAY.equals(this.taskType)) {
      SchedulerService.getInstance().scheduleWithFixedDelay(this.task, this.reschedulePeriod, reschedulePeriod, unit);
    }
  }
}
