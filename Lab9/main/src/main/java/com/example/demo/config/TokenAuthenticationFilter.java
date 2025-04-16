package com.example.demo.config;

import com.example.demo.dto.user.UserInfo;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.auth.AuthClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Value("${auth.service.validate-url}")
    private String authValidateUrl;

    @Autowired
    private AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        // Пропускаем Swagger и публичные endpoints
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") || path.startsWith("/api/public")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        logger.error("AuthHeader: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.error("Token: " + token);
            try {
                // Используем Feign Client для валидации токена и получения информации о пользователе
                String token_val = "Bearer " + token;
                logger.error("Token received for validation: " + token_val);
                UserInfo userInfo = authClient.validateToken(token_val);

                if (userInfo != null) {
                    logger.error("userInfo: " + userInfo.getUsername());
                    // Если токен валиден, извлекаем данные о пользователе и создаём аутентификацию
                    UserEntity user = userRepository.findByUsername(userInfo.getUsername());  // Здесь будет ваша логика поиска пользователя

                    if (user != null) {
                        // Создаём объект аутентификации
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRole()))
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Устанавливаем аутентификацию в контекст безопасности
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        // Добавляем userId в атрибуты запроса для дальнейшего использования
                        request.setAttribute("userId", user.getId());
                    } else {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "User not found");
                        return;
                    }
                } else {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token");
                    return;
                }
            } catch (Exception e) {
                logger.error("Token validation failed", e);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
                return;
            }
        } else {

            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing authorization header");
            return;
        }

        filterChain.doFilter(request, response);
    }
}