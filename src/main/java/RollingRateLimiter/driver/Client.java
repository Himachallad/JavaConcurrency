package RollingRateLimiter.driver;

import RollingRateLimiter.enums.RateLimiterType;
import RollingRateLimiter.factory.RateLimiterFactoryImpl;
import RollingRateLimiter.factory.SimpleRateLimiterFactory;
import RollingRateLimiter.ratelimiters.RateLimiter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
  static ExecutorService executorService = Executors.newFixedThreadPool(10);
  public static void main(String[] args) throws InterruptedException {
    SimpleRateLimiterFactory simpleRateLimiterFactory = new RateLimiterFactoryImpl();
    RateLimiter rateLimiter = simpleRateLimiterFactory.createRateLimiter(RateLimiterType.SLIDING);

    rateLimiter.registerClient("client1", 10);

    executorService.submit(executeWithSleep(5000, rateLimiter, "client1"));
    executorService.submit(executeWithSleep(1000, rateLimiter, "client1"));
    executorService.submit(executeWithSleep(2000, rateLimiter, "client1"));
    executorService.submit(executeWithSleep(3000, rateLimiter, "client1"));
    executorService.submit(executeWithSleep(4000, rateLimiter, "client1"));



  }

  private static Runnable executeWithSleep(int millis, RateLimiter rateLimiter, String clientId) {
    return () -> {
      for (int i = 0; i < 100; i++) {

        try {
          Thread.sleep(millis);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        rateLimiter.allowRequest(clientId);
      }
    };
  }
}
