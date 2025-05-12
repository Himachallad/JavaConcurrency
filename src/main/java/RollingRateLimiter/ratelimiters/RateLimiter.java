package RollingRateLimiter.ratelimiters;

public interface RateLimiter {

  boolean registerClient(String clientId, int requestLimit);
  boolean allowRequest(String clientId);
}
