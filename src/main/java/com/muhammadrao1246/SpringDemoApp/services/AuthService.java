package com.muhammadrao1246.SpringDemoApp.services;

import com.muhammadrao1246.SpringDemoApp.factory.OAuthProviderFactory;
import com.muhammadrao1246.SpringDemoApp.models.DTO.UserJwtTokenDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.UserLoginDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.UserRegisterDto;

import com.muhammadrao1246.SpringDemoApp.models.User;
import com.muhammadrao1246.SpringDemoApp.models.UserLinkedAuthProvider;
import com.muhammadrao1246.SpringDemoApp.repositories.UserRolesRepository;
import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import com.muhammadrao1246.SpringDemoApp.providers.OAuthProvider;
import com.muhammadrao1246.SpringDemoApp.repositories.UserLinkedAuthProviderRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.UserRepository;
import com.muhammadrao1246.SpringDemoApp.utils.TokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserLinkedAuthProviderRepository userLinkedAuthProviderRepository;
    private final UserRolesRepository userRolesRepository;

    public void forgotPassword(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User not found with this email."));

        // if found the prepare a forgot message
    }

    public UserJwtTokenDto login(UserLoginDto dto) {
        // first we will use the authentication manager to authenticate the user
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        // if authenticated we going to get user in the principal object
        User user = (User) auth.getPrincipal();

        System.out.println(user);
        System.out.println(auth.getAuthorities());

        // here we are going to generate the token
        return tokenUtils.generateJWTTokens(user);
    }

    @Transactional
    public User register(UserRegisterDto dto) {
        // validations is both password same
        if(!dto.getPassword().equals(dto.getConfirmPassword())) throw new IllegalArgumentException("Passwords do not match");

        // if a user exists already then throw exception

        // in register just create the user in db and send ok to response
        User user = User.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .roles(userRolesRepository.findAllByRoles(dto.getRoles()))
                .build();

        // try to save in use repository if failed than means a user exists
        try{
            user = userRepository.save(user);
        }
        catch(Exception e){throw e;}

        return user;
    }

    // handle OAuth2 login attempt
    @Transactional
    public UserJwtTokenDto handleOAuth2LoginAttempt(String registrationId, OAuth2User oAuth2User){
        // obtaining Provider Type
        AppAuthProviders providerType = OAuthProviderFactory.getAuthProviderType(registrationId);

        // oauth2 factory
        OAuthProviderFactory factory = new OAuthProviderFactory(providerType);

        // getting appropriate provider for OAuth2User
        OAuthProvider provider = factory.getProvider(oAuth2User);

        // fetching provider assigned id to user
        String userProviderId = provider.getId();

        // fetching necessary details
        String username = provider.getUsername();
        String email = provider.getEmail();
        String name = Optional.ofNullable(provider.getName()).orElse("User");

        // finding if user exists with this provider already
        String authProviderId = providerType.name()+":"+userProviderId;
        UserLinkedAuthProvider linkedAuthProvider = userLinkedAuthProviderRepository.findById(authProviderId).orElse(null);

        // User to be created or to be found
        User user = null;

        // if already login with provider then straight login
        if(linkedAuthProvider != null){
            user = linkedAuthProvider.getUser();
        }
        else{
            // finding if user already exists
            user = userRepository.findByUsernameOrEmail(username, email).orElse(null);

            // if user is already there then we just bind it to user existing account automatically
            if(user != null && user.getEmail() != null && user.getEmail().equals(email)){
                linkedAuthProvider = new UserLinkedAuthProvider(authProviderId, user);
                user.getLinkedAccounts().add(linkedAuthProvider);
            }
            else{
                // if the user is still found maybe there is a similar username then we should not set username random
                if (user != null) {
                    username = null;
                }

                // if user not logged in with this provider
                // and both username and email are possible
                user = User.builder()
                        .name(name)
                        .username(username)
                        .email(email) // email will be attached if not then null
                        .password(null)
                        .linkedAccounts(new ArrayList<>())
                        .roles(
                                Set.of(
                                        userRolesRepository.findByRole(RoleTypes.CUSTOMER).orElseThrow(()->new EntityNotFoundException("Role Not Found"))
                                )
                        )
                        .build();

                // then add provider to it
                user.getLinkedAccounts().add(new UserLinkedAuthProvider(authProviderId, user));

                // now create user
                user = userRepository.save(user);
            }
        }


        // now authenticate and save info in security context and return the generated token
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        // generate token and send back the dto
        return tokenUtils.generateJWTTokens(user);
    }
}
