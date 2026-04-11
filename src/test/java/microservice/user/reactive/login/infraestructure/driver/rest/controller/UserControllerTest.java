package microservice.user.reactive.login.infraestructure.driver.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.user.reactive.login.domain.exception.ValidationException;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.ports.in.IUserPortUseCase;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.UserUpdateRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.mapper.UserRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static microservice.user.reactive.login.domain.exception.error.CommonErrorCode.USER_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserPortUseCase userUseCase;

    @MockBean
    private UserRestMapper userRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_Success() throws Exception {
        UserModel user = new UserModel();
        UserResponse response = new UserResponse();
        response.setName("Juan");

        when(userUseCase.getAllUsers()).thenReturn(Collections.singletonList(user));
        when(userRestMapper.toUserResponse(any())).thenReturn(response);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    void getById_Success() throws Exception {
        UserModel user = new UserModel();
        UserResponse response = new UserResponse();
        response.setId(1L);

        when(userUseCase.getUserById(1L)).thenReturn(user);
        when(userRestMapper.toUserResponse(user)).thenReturn(response);

        mockMvc.perform(get("/api/users/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getByDni_Success() throws Exception {
        UserModel user = new UserModel();
        UserResponse response = new UserResponse();
        response.setDni("12345");

        when(userUseCase.getUserByDni("12345")).thenReturn(user);
        when(userRestMapper.toUserResponse(user)).thenReturn(response);

        mockMvc.perform(get("/api/users/dni/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345"));
    }

    @Test
    void getByName_Success() throws Exception {
        UserModel user = new UserModel();
        UserResponse response = new UserResponse();
        response.setName("Juan");

        when(userUseCase.getUsersByName("Juan")).thenReturn(List.of(user));
        when(userRestMapper.toUserResponse(user)).thenReturn(response);

        mockMvc.perform(get("/api/users/search").param("name", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    void update_Success() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("New Name");
        UserModel userModel = new UserModel();
        UserResponse response = new UserResponse();
        response.setName("New Name");

        when(userRestMapper.toUserModel(any(UserUpdateRequest.class))).thenReturn(userModel);
        when(userUseCase.updateUser(any(UserModel.class), eq(1L))).thenReturn(userModel);
        when(userRestMapper.toUserResponse(userModel)).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void delete_Success() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getById_NotFound_ShouldReturn404() throws Exception {
        // En CommonErrorCode, USER_NOT_FOUND tiene statusCode 404
        when(userUseCase.getUserById(1L)).thenThrow(new ValidationException(USER_NOT_FOUND));

        mockMvc.perform(get("/api/users/id/1"))
                .andExpect(status().isNotFound());
    }
}
