package microservice.user.reactive.login.infraestructure.driver.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.user.reactive.login.domain.ports.in.IAuthPortUseCase;
import microservice.user.reactive.login.domain.models.TokenModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.LoginRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.RegisterRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.TokenRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.TokenResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.mapper.AuthRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthPortUseCase authUseCase;

    @MockBean
    private AuthRestMapper authRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "Password123");
        TokenModel tokenModel = TokenModel.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .expiresIn(3600L)
                .build();
        TokenResponse response = new TokenResponse("access", "refresh", 3600L);

        when(authRestMapper.toAuthModel(any())).thenReturn(null);
        when(authUseCase.authenticateUser(any())).thenReturn(tokenModel);
        when(authRestMapper.toTokenResponse(tokenModel)).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access"))
                .andExpect(jsonPath("$.refreshToken").value("refresh"));
    }

    @Test
    void register_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.com");
        UserModel userModel = new UserModel();
        userModel.setEmail("test@test.com");
        UserResponse response = new UserResponse();
        response.setEmail("test@test.com");

        when(authRestMapper.toUserModel(any())).thenReturn(userModel);
        when(authUseCase.register(any())).thenReturn(userModel);
        when(authRestMapper.toUserResponse(userModel)).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void revoke_Success() throws Exception {
        TokenRequest request = new TokenRequest();
        request.setToken("some-token");

        when(authUseCase.revokeToken("some-token")).thenReturn(true);

        mockMvc.perform(post("/api/auth/revoke")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void validate_Success() throws Exception {
        TokenRequest request = new TokenRequest();
        request.setToken("some-token");

        when(authUseCase.validateToken("some-token")).thenReturn(true);

        mockMvc.perform(post("/api/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
