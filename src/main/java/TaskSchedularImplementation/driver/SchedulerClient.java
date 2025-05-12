package TaskSchedularImplementation.driver;

import TaskSchedularImplementation.main.service.SchedulerService;
import TaskSchedularImplementation.main.service.SchedulerServiceImpl;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerClient {

  public static void main(String[] args) {
    Runnable task1 = () -> {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.println("Task1 is executed!!");

    };

    Runnable task2 = () -> System.out.println("Task2 is executed!!");
    Callable x = () -> {
      System.out.println("123");
      return "done";
    };
    /*
    JAVA IMPLEMENTATION
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    scheduledExecutorService.scheduleWithFixedDelay(task1, 2, 2, TimeUnit.SECONDS);
    scheduledExecutorService.scheduleAtFixedRate(task2, 2, 5, TimeUnit.SECONDS);
    scheduledExecutorService.schedule(x, 2, TimeUnit.SECONDS);

     */

    SchedulerService schedulerService = SchedulerServiceImpl.getInstance();

//    schedulerService.schedule(task1, 2, TimeUnit.SECONDS);
    schedulerService.scheduleWithFixedRate(task1, 4, 2, TimeUnit.SECONDS);
    schedulerService.schedule(task2, 0, TimeUnit.SECONDS);
    schedulerService.scheduleWithFixedDelay(task2, 4, 10, TimeUnit.SECONDS);
  }
}
