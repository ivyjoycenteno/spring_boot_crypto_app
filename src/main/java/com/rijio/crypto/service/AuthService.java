package com.rijio.crypto.service;

import com.rijio.crypto.payload.LoginDTO;
import com.rijio.crypto.payload.SignupDTO;
import com.rijio.crypto.payload.UserDTO;

public interface AuthService {
    UserDTO registerUser(SignupDTO signupDTO);
    String authenticateUser(LoginDTO loginDTO);
}
