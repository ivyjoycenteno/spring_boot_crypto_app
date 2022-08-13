package com.rijio.crypto.unit.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.rijio.crypto.controller.AuthController;
import com.rijio.crypto.payload.SignupDTO;
import com.rijio.crypto.payload.UserDTO;
import com.rijio.crypto.repository.UserRepository;
import com.rijio.crypto.service.AuthService;
import com.rijio.crypto.util.JsonUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks // This allows to inject Mock objects.
    private AuthController authController;

    @MockBean
    private AuthService authService;
    
    @Mock
    private UserRepository userRepository;

    SignupDTO userSignupDTO;

    @BeforeEach
	void setUp() throws Exception
	{
		/*
		 *  This is needed for Mockito to be able to instantiate the Mock Objects
		 *  and Inject into the userController object
		 */
		MockitoAnnotations.openMocks(this);

        userSignupDTO = new SignupDTO();
        userSignupDTO.setName("Ij Rij");
        userSignupDTO.setUsername("ijrij");
        userSignupDTO.setEmail("ijrij@rij.com");
        userSignupDTO.setPassword("password");
        
    }

    @Test
    void registerUser_whenPostMethod() throws Exception {

        var TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
        var httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        var csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        ModelMapper mapper = new ModelMapper();
        UserDTO userDTO = mapper.map(userSignupDTO, UserDTO.class);


        given(userRepository.existsByUsername(anyString())).willReturn(null);
        given(userRepository.existsByEmail(anyString())).willReturn(null);

        // given(authService.registerUser(userSignupDTO)).willReturn(userDTO);

        mockMvc.perform(post("/api/v1/auth/signup")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userSignupDTO)))
                .andDo((print()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(userDTO.getName())));

        verify(authService).registerUser(userSignupDTO);
    }

    // @Test
    // void loginUser_whenPostMethod() throws Exception {

    // }
    
}
