package akin.backend.ratelimit.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimiterFilter extends OncePerRequestFilter {
    private final RateLimitService rateLimitService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (rateLimitService.isLimitExceeded(ip, path, method)) {
            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
            return;
        }
        filterChain.doFilter(request, response);

    }


}
