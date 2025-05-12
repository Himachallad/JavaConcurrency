package RollingRateLimiter.ratelimiters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketRateLimiter implements RateLimiter {
  Map<String, Integer> clientRateLimit;
  Map<String, AtomicInteger> clientBucketSize;
  private ScheduledExecutorService scheduledExecutorService;

  private static final int MAX_BUCKET_SIZE = 10;

  public TokenBucketRateLimiter() {
    clientRateLimit = new ConcurrentHashMap<>();
    clientBucketSize = new ConcurrentHashMap<>();
    scheduledExecutorService = Executors.newScheduledThreadPool(4);
    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
  }

  private void refillBucket(String clientId) {
    clientBucketSize.computeIfPresent(clientId, (key, bucket) -> {
      int newSize = Math.min(MAX_BUCKET_SIZE, bucket.incrementAndGet());
      System.out.println("Refilling bucket for client: " + clientId + " to size: " + newSize);
      return bucket;
    });
  }

  @Override
  public boolean registerClient(String clientId, int requestLimit) {
    clientRateLimit.put(clientId, requestLimit);
    clientBucketSize.put(clientId, new AtomicInteger(MAX_BUCKET_SIZE));

    // Convert requestLimit to RPM and calculate bucket refill time
    long bucketRefillTimeInMs = (long) Math.ceil(60000.0 / requestLimit);
    scheduledExecutorService.scheduleAtFixedRate(() -> refillBucket(clientId), bucketRefillTimeInMs,
          bucketRefillTimeInMs, TimeUnit.MILLISECONDS);

    return true;
  }

  @Override
  public boolean allowRequest(String clientId) {
    AtomicInteger bucketSize = clientBucketSize.get(clientId);
    if (bucketSize == null) {
      System.out.println("Request denied for client: " + Thread.currentThread().getName() + clientId);
      return false;
    }

    int currentSize = bucketSize.getAndDecrement();
    if (currentSize <= 0) {
      bucketSize.incrementAndGet(); // Restore previous value
      System.out.println("Request denied for client: " + Thread.currentThread().getName() + clientId);
      return false;
    }

    System.out.println("Request allowed for client: " + Thread.currentThread().getName() + clientId);
    return true;

  }

  public void shutdown() {
    scheduledExecutorService.shutdown();
    try {
      if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduledExecutorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduledExecutorService.shutdownNow();
    }
  }
}
