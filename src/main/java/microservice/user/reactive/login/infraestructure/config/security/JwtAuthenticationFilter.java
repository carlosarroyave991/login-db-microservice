package microservice.user.reactive.login.infraestructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.ports.out.AuthPersistencePort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final AuthPersistencePort authPersistencePort;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (jwtBlacklistService.isBlacklisted(token) || !jwtUtil.validateToken(token)) {
            writeUnauthorized(response, "Invalid or expired token");
            return;
        }

        try {
            String sessionId = jwtUtil.getSessionIdFromToken(token);
            boolean valid = authPersistencePort.findByTokenHash(sessionId)
                    .map(rt -> !Boolean.TRUE.equals(rt.getRevoked())
                            && rt.getExpiresAt() != null
                            && rt.getExpiresAt().isAfter(OffsetDateTime.now()))
                    .orElse(false);

            if (!valid) {
                writeUnauthorized(response, "Token revoked or session expired");
                return;
            }

            Authentication auth = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            writeUnauthorized(response, "Token processing error");
            return;
        }

        chain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),
                Map.of("code", "ERR_UNAUTHORIZED", "message", message));
    }
}
