package akin.backend.ratelimit.filter;

import akin.backend.ratelimit.redis.RedisService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisService redisService;

    @Value("${rate-limiter.window-seconds}")
    private long windowSeconds;

    @Value("${rate-limiter.max-requests}")
    private long maxRequests;

    public boolean isLimitExceeded(String ip, String path, String method) {
        try {
            String key = ip + ":" + path + ":" + method;

            Long count = redisService.increment(key);

            if (count == 1) {
                redisService.setExpire(key, windowSeconds);
                return false;
            }
            return count > maxRequests;
        } catch (Exception e) {
            return false;
        }

    }
}
