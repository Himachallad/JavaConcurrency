package RollingRateLimiter.ratelimiters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter {
  private ScheduledExecutorService taskSchedulerService;
  private Map<String, AtomicInteger> requestAllowedPerClientMap;
  private final Map<String, ScheduledFuture<?>> clientResetTasks;

  public FixedWindowRateLimiter() {
    taskSchedulerService = Executors.newScheduledThreadPool(4);
    requestAllowedPerClientMap = new ConcurrentHashMap<>();
    clientResetTasks = new ConcurrentHashMap<>();
  }
  @Override
  public boolean registerClient(String clientId, int requestLimit) {
    requestAllowedPerClientMap.computeIfAbsent(clientId, k -> new AtomicInteger(requestLimit));

    clientResetTasks.compute(clientId, (k, existingTask) -> {
      if (existingTask != null) {
        existingTask.cancel(false);
      }
      return taskSchedulerService.scheduleAtFixedRate(() -> {
        requestAllowedPerClientMap.computeIfPresent(clientId, (key, val) -> {
          val.set(requestLimit);
          return val;
        });
        System.out.printf("\n\n\nResetting request limit for client: %s to %d \n\n\n", clientId, requestLimit);
      }, 0, 1, TimeUnit.MINUTES);
    });
    return true;
  }

  @Override
  public boolean allowRequest(String clientId) {
    AtomicInteger requestAllowedPerMin = requestAllowedPerClientMap.get(clientId);
    if (requestAllowedPerMin == null) {
      System.out.println("Request denied for client: " + Thread.currentThread().getName() + clientId);
      return false;
    }
    int currentRequestAllowed = requestAllowedPerMin.getAndDecrement();
    if (currentRequestAllowed <= 0) {
      requestAllowedPerMin.incrementAndGet();
      System.out.println("Request denied for client: " + Thread.currentThread().getName() + clientId);
      return false;
    }
    System.out.println("Request allowed for client: " + Thread.currentThread().getName() + clientId);
    return true;
  }
}
