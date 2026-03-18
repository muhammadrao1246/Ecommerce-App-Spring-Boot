package com.muhammadrao1246.SpringDemoApp.configs;

import com.muhammadrao1246.SpringDemoApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// custom implementation of UserDetailService Manager that was previously used
// by built in InMemory and other managers
// here we are providing our implementation to Spring Security
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username, username).orElseThrow();
    }
}
