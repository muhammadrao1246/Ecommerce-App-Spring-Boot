package com.muhammadrao1246.SpringDemoApp.handlers;

import com.muhammadrao1246.SpringDemoApp.models.DTO.UserJwtTokenDto;
import com.muhammadrao1246.SpringDemoApp.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // fetching OAuthUserAuthentication
        OAuth2AuthenticationToken tokenResponse = (OAuth2AuthenticationToken) authentication;

        // getting provider id
        String registrationId = tokenResponse.getAuthorizedClientRegistrationId();

        // extracting OAuth2User
        OAuth2User user = (OAuth2User) tokenResponse.getPrincipal();

        // getting several attributes from OAuth2User to create user
        UserJwtTokenDto tokenDto = authService.handleOAuth2LoginAttempt(registrationId, user);


        log.info("User logged in successfully: {}", user);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), tokenDto);
        response.getWriter().flush();
    }
}
