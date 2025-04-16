package com.example.demo.config;

import com.example.demo.entity.user.TokenEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.user.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;

    public TokenAuthenticationFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Пропускаем публичные endpoints и Swagger
        if (path.startsWith("/api/auth") || path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            tokenRepository.findByToken(token)
                    .filter(tokenEntity -> !isTokenExpired(tokenEntity))
                    .ifPresentOrElse(
                            tokenEntity -> authenticateUser(tokenEntity, request),
                            () -> {
                                try {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        } catch (Exception e) {
            logger.error("Authentication error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenExpired(TokenEntity tokenEntity) {
        return tokenEntity.getExpiresAt().before(new Timestamp(System.currentTimeMillis()));
    }

    private void authenticateUser(TokenEntity tokenEntity, HttpServletRequest request) {
        UserEntity user = tokenEntity.getUser();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(new SimpleGrantedAuthority(user.getRole().getRole()))
                );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Обновляем время последнего использования токена (опционально)
        tokenRepository.save(tokenEntity);
    }
}