package TaskSchedularImplementation.main.service;

import java.util.concurrent.TimeUnit;

public interface SchedulerService {
  public static SchedulerService getInstance() {
    return SchedulerServiceImpl.getInstance();
  }
  public void schedule(Runnable task, long initialDelay, TimeUnit timeUnit);
  public void scheduleWithFixedDelay(Runnable task, long initialDelay, long fixedDelay, TimeUnit timeUnit);
  public void scheduleWithFixedRate(Runnable task, long initialDelay, long fixedRate, TimeUnit timeUnit);

}
