package com.rijio.crypto.service.impl;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rijio.crypto.entity.Role;
import com.rijio.crypto.entity.User;
import com.rijio.crypto.exception.CryptoAPIException;
import com.rijio.crypto.payload.LoginDTO;
import com.rijio.crypto.payload.SignupDTO;
import com.rijio.crypto.payload.UserDTO;
import com.rijio.crypto.repository.RoleRepository;
import com.rijio.crypto.repository.UserRepository;
import com.rijio.crypto.security.JWTTokenProvider;
import com.rijio.crypto.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    
    public AuthServiceImpl(){
    }

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(SignupDTO signupDTO) {

        // check existing user
        if(userRepository.existsByUsername(signupDTO.getUsername())) {
            throw new CryptoAPIException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }

        // check existing email
        if(userRepository.existsByEmail(signupDTO.getEmail())) {
            throw new CryptoAPIException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }

        User user = new User();
        user.setName(signupDTO.getName());
        user.setUsername(signupDTO.getUsername());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        ModelMapper mapper = new ModelMapper();
        User newUser = userRepository.save(user);
        UserDTO userDTO = mapper.map(newUser, UserDTO.class);
        
        return userDTO;
    }

    @Override
    public String authenticateUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // retrieve generated token from provider
        String accessToken = jwtTokenProvider.generateToken(authentication);
        return accessToken;
    }
    
}
