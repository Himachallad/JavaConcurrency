package RollingRateLimiter.factory;

import RollingRateLimiter.enums.RateLimiterType;
import RollingRateLimiter.ratelimiters.FixedWindowRateLimiter;
import RollingRateLimiter.ratelimiters.LeakyRateLimiter;
import RollingRateLimiter.ratelimiters.RateLimiter;
import RollingRateLimiter.ratelimiters.SlidingWindowRateLimiter;
import RollingRateLimiter.ratelimiters.TokenBucketRateLimiter;

public class RateLimiterFactoryImpl implements SimpleRateLimiterFactory {
  @Override
  public RateLimiter createRateLimiter(RateLimiterType type) {
    switch (type) {
      case FIXED:
        return new FixedWindowRateLimiter();
      case LEAKY:
        return new LeakyRateLimiter();
      case SLIDING:
        return new SlidingWindowRateLimiter();
      case TOKEN_BUCKET:
        return new TokenBucketRateLimiter();
    }
    throw new IllegalArgumentException("Invalid rate limiter type");
  }
}
