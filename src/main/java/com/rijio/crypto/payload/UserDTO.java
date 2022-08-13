package com.rijio.crypto.payload;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String name;
    private String username;
    private String email;
}
