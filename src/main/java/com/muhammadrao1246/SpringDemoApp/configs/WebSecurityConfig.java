package com.muhammadrao1246.SpringDemoApp.configs;

import com.muhammadrao1246.SpringDemoApp.handlers.OAuthSuccessHandler;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes.*;
import static com.muhammadrao1246.SpringDemoApp.models.types.PermissionTypes.*;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

//    private final PasswordEncoder passwordEncoder;
//    private final JWTSecurityVerificationFilter jwtFilter;
//    private final HandlerExceptionResolver handlerExceptionResolver;
//    private final OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTSecurityVerificationFilter jwtFilter, HandlerExceptionResolver handlerExceptionResolver, OAuthSuccessHandler oAuthSuccessHandler) throws Exception {
        System.out.println("Security Configured");
        httpSecurity.formLogin(Customizer.withDefaults()); // using defaults forms settings
        // can be modified for setting confirm, reset, signup, login pages and their parameters
        // currently disabling it below
        httpSecurity.formLogin(login -> login.disable());
        // disabling csrf as we are going stateless
        httpSecurity.csrf(csrf -> csrf.disable());
        // disabling session maangement
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // removing all restriction altogether
        httpSecurity.authorizeHttpRequests(auth -> auth
//            .anyRequest().authenticated() // all will be only visible to authenticated users
                // below is public matchers
              .requestMatchers("/auth/**").permitAll() // a dummy to explain

                // OLD: ----- product, brand, categpry can be created by admin, seller but only seen by customer
                // NEW: ----- after switching to permission it is much easy to loosely couple the code to the roles we can
                // create more roles later and assign them their required permission for their routes

                // OLD: ----- Orders can be created by Customer, Admin, Seller
                // NEW: -----Also permissions can be added or removed from a role easily without affecting the code
                        .requestMatchers(HttpMethod.GET, "/products/**").hasAuthority("PRODUCT:READ")
                        .requestMatchers(HttpMethod.GET, "/brand/**").hasAuthority("BRAND:READ")
                        .requestMatchers(HttpMethod.GET, "/category/**").hasAuthority("CATEGORY:READ")
                        .requestMatchers(HttpMethod.GET, "/orders/**").hasAuthority("ORDERS:READ")
                        .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority("PRODUCT:CREATE")
                        .requestMatchers(HttpMethod.POST, "/brand/**").hasAuthority("BRAND:CREATE")
                        .requestMatchers(HttpMethod.POST, "/category/**").hasAuthority("CATEGORY:CREATE")
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasAuthority("ORDERS:CREATE")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority("PRODUCT:UPDATE")
                        .requestMatchers(HttpMethod.PUT, "/brand/**").hasAuthority("BRAND:UPDATE")
                        .requestMatchers(HttpMethod.PUT, "/category/**").hasAuthority("CATEGORY:UPDATE")
                        .requestMatchers(HttpMethod.PUT, "/orders/**").hasAuthority("ORDERS:UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority("PRODUCT:DELETE")
                        .requestMatchers(HttpMethod.DELETE, "/brand/**").hasAuthority("BRAND:DELETE")
                        .requestMatchers(HttpMethod.DELETE, "/category/**").hasAuthority("CATEGORY:DELETE")
                        .requestMatchers(HttpMethod.DELETE, "/orders/**").hasAuthority("ORDERS:DELETE")

              // all other testing related or other requests all only permitted to ADMIN
              .anyRequest().hasRole(ADMIN.name()) // permitting all requests
        );
        // we are going to add our custom Jwt filter for validation of incoming AUthroization header on protected requests
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // adding exception handler resolver
        httpSecurity.exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, exception)->
                        handlerExceptionResolver.resolveException(request, response, null, exception)
                )
        );
        // adding oauth2 client support
        httpSecurity.oauth2Login(oauth -> oauth
                .failureHandler((request, response, exception) -> {
                    log.error("OAUTH2 LOGIN ERROR: {}", exception.getMessage());
                })
                .successHandler(oAuthSuccessHandler)
        );
        
        return httpSecurity.build();
    }

    // we need an AuthenticationManager for our application users to get authenticated by Providers with Custom implementations
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Creating or Adding Users in default InMemoryUserDetailManager
    // Now it is invalid cause now we are going to use customizations
//    @Bean
//    UserDetailsService userDetailsService(){
//        UserDetails user1 = User.builder().username("admin").password(passwordEncoder.encode("pass")).roles("ADMIN").build();
//        UserDetails user2 = User.builder().username("user1").password(passwordEncoder.encode("pass")).roles("USER").build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }
}
