package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.DTO.UserJwtTokenDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.UserLoginDto;
import com.muhammadrao1246.SpringDemoApp.models.DTO.UserRegisterDto;
import com.muhammadrao1246.SpringDemoApp.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserJwtTokenDto> loginUser(@RequestBody @Valid UserLoginDto dto){
        // attempting to login using auth service
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterDto dto){
        // attempting to register using auth service
        authService.register(dto);
        return ResponseEntity.ok("User Registered Successfully!");
    }
}
