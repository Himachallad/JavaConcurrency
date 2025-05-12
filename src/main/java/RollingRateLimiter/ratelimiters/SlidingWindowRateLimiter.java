package RollingRateLimiter.ratelimiters;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter implements RateLimiter {
  private static class ClientState {
    final ConcurrentLinkedDeque<Long> queue = new ConcurrentLinkedDeque<>();
    final AtomicInteger count = new AtomicInteger(0);
    final AtomicInteger limit;

    ClientState(int limit) {
      this.limit = new AtomicInteger(limit);
    }
  }

  private final Map<String, ClientState> clientStates = new ConcurrentHashMap<>();

  @Override
  public boolean registerClient(String clientId, int requestLimit) {
    clientStates.compute(clientId, (k, oldState) -> {
      if (oldState != null) {
        System.out.printf("Overriding client %s from %s to %s%n", clientId, oldState.limit.get(), requestLimit);
      }
      return new ClientState(requestLimit);
    });
    return true;

  }

  @Override
  public boolean allowRequest(String clientId) {
    long currentTime = System.currentTimeMillis();
    ClientState state = clientStates.get(clientId);
    if (state == null) {
      System.out.println("Client not registered: " + clientId);
      return false;
    }

    synchronized (state.queue) {
      // state.queue.isEmpty() is thread safe inside synchronized block
      while (!state.queue.isEmpty()) {
        Long timestamp = state.queue.peek();
        if (timestamp == null || timestamp >= currentTime - 60000) break;
        state.queue.poll();
        state.count.decrementAndGet();
      }

      // Check limit
      if (state.count.get() >= state.limit.get()) {
        System.out.printf("Request denied for client: %s %s %d\n", Thread.currentThread().getName(), clientId, Calendar.getInstance().get(Calendar.SECOND));
        return false;
      }

      // Add request
      state.queue.add(currentTime);
      state.count.incrementAndGet();
      System.out.printf("Request allowed for client: %s %s %d\n", Thread.currentThread().getName(), clientId, Calendar.getInstance().get(Calendar.SECOND));
      return true;
    }
  }
}
