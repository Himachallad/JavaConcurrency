package RollingRateLimiter.ratelimiters;

import java.util.concurrent.TimeUnit;

public class LeakyRateLimiter implements RateLimiter {

  @Override
  public boolean registerClient(String clientId, int requestLimit) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public boolean allowRequest(String clientId) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
