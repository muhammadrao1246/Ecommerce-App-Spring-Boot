package com.muhammadrao1246.SpringDemoApp.configs;

import com.muhammadrao1246.SpringDemoApp.models.User;
import com.muhammadrao1246.SpringDemoApp.repositories.UserRepository;
import com.muhammadrao1246.SpringDemoApp.utils.TokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTSecurityVerificationFilter extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            // logging
            log.info("Incoming Request: {}", request.getRequestURI());
            // just pass ON if they are public routes
            String path = request.getServletPath();
            if(path.startsWith("/api/v1/auth/")) {
                log.info("Skipping JWT Security Filter");
                doFilter(request, response, filterChain);
                return;
            }

            // getting token from header
            String token = request.getHeader("Authorization");

            // if token is not present
            if(token == null || !token.startsWith("Bearer ")) {
                doFilter(request, response, filterChain);
                return;
            }
            // fetching use id if token is valid
            UUID verifiedUserId = tokenUtils.getUserIdFromToken(token);

            // if verification fails
            if(verifiedUserId == null){
                doFilter(request, response, filterChain);
                return;
            };

            // if authentication is not set but the token is valid
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                User user = userRepository.findById(verifiedUserId).orElseThrow(()->new EntityNotFoundException("User not found"));
                log.info(user.toString());
                log.info(user.getAuthorities().toString());
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
                );
            }
            log.info(verifiedUserId.toString());
            doFilter(request, response, filterChain);
        }
        // catching errors that are going to occur in other filters next here
        catch (Exception e){
            log.info(e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
