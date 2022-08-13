package com.rijio.crypto.payload;

import lombok.Data;

@Data
public class SignupDTO {
    private String name;
    private String username;
    private String email;
    private String password;
}
