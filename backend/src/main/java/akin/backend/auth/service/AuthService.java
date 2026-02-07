package akin.backend.auth.service;

import akin.backend.auth.exception.InvalidCredentialsException;
import akin.backend.auth.exception.RateLimitExceededException;
import akin.backend.auth.jwt.JwtUtil;
import akin.backend.auth.request.LoginRequest;
import akin.backend.auth.response.AuthResponse;
import akin.backend.ratelimit.redis.RedisService;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final String LOGIN_RATE_KEY_PREFIX = "login:rate:";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Value("${rate-limiter.login-window-seconds:300}")
    private long loginWindowSeconds;

    @Value("${rate-limiter.login-max-attempts:5}")
    private long loginMaxAttempts;

    public AuthResponse login(LoginRequest request, Role requiredRole) {
        String key = LOGIN_RATE_KEY_PREFIX + request.getUsername();
        Long attempts = redisService.increment(key);
        if (attempts == 1) {
            redisService.setExpire(key, loginWindowSeconds);
        }
        if (attempts > loginMaxAttempts) {
            log.warn("Login rate limit exceeded for user: {}", request.getUsername());
            throw new RateLimitExceededException();
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (user.getRole() != requiredRole) {
            throw new InvalidCredentialsException();
        }

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtUtil.generateToken(user.getId(), user.getUsername(), Set.of(user.getRole()));
        log.info("Kullanıcı giriş yaptı: {} rol: {}", user.getUsername(), requiredRole);

        return new AuthResponse(accessToken);
    }
}
