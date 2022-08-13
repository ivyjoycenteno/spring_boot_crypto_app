package com.rijio.crypto.unit.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rijio.crypto.entity.Role;
import com.rijio.crypto.entity.User;
import com.rijio.crypto.repository.RoleRepository;
import com.rijio.crypto.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void testSignup() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");

        User user = new User();
        user.setName("Ij Rij");
        user.setUsername("ijrij");
        user.setEmail("ijrij@rij.com");
        user.setPassword("%$%^%^^%^^%^%$%$%$&");
        user.setRoles(Collections.singleton(role));

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
		when(passwordEncoder.encode(anyString())).thenReturn("%$%^%^^%^^%^%$%$%$&");
		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User newUser = userRepository.save(user);
        MatcherAssert.assertThat(newUser.getUsername(), equalTo(user.getUsername()));
        verify(userRepository).save(user);
    }
}