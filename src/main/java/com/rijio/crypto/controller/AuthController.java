package com.rijio.crypto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rijio.crypto.payload.JWTAuthResponse;
import com.rijio.crypto.payload.LoginDTO;
import com.rijio.crypto.payload.SignupDTO;
import com.rijio.crypto.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO loginDTO) {

        String accessToken = authService.authenticateUser(loginDTO);

        return ResponseEntity.ok(new JWTAuthResponse(accessToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupDTO signupDTO) {

        authService.registerUser(signupDTO);

        return new ResponseEntity<>("User has been successfully registered.", HttpStatus.OK);
    }
    
}
