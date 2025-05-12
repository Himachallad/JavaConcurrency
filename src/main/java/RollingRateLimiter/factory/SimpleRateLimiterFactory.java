package RollingRateLimiter.factory;

import RollingRateLimiter.enums.RateLimiterType;
import RollingRateLimiter.ratelimiters.RateLimiter;

public interface SimpleRateLimiterFactory {
  RateLimiter createRateLimiter(RateLimiterType type);
}
