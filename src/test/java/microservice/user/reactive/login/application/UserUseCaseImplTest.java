package microservice.user.reactive.login.application;

import microservice.user.reactive.login.domain.exception.ValidationException;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.ports.out.UserPersistencePort;
import microservice.user.reactive.login.domain.service.DniValidationService;
import microservice.user.reactive.login.domain.service.EmailValidationService;
import microservice.user.reactive.login.domain.service.PhoneValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseImplTest {

    @Mock
    private UserPersistencePort userPersistencePort;
    @Mock
    private EmailValidationService emailValidationService;
    @Mock
    private PhoneValidationService phoneValidationService;
    @Mock
    private DniValidationService dniValidationService;

    @InjectMocks
    private UserUseCaseImpl userUseCase;

    @Test
    void getUserById_Success() {
        UserModel user = new UserModel();
        user.setId(1L);
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(user));

        UserModel result = userUseCase.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_NotFound_ShouldThrowException() {
        when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> userUseCase.getUserById(1L));
    }

    @Test
    void getUserByDni_Success() {
        UserModel user = new UserModel();
        user.setDni("12345678");
        when(dniValidationService.isValidDni("12345678")).thenReturn(true);
        when(userPersistencePort.findByDni("12345678")).thenReturn(Optional.of(user));

        UserModel result = userUseCase.getUserByDni("12345678");

        assertNotNull(result);
        assertEquals("12345678", result.getDni());
    }

    @Test
    void getUserByDni_InvalidDni_ShouldThrowException() {
        when(dniValidationService.isValidDni("invalid")).thenReturn(false);

        assertThrows(ValidationException.class, () -> userUseCase.getUserByDni("invalid"));
    }

    @Test
    void getUserByDni_NotFound_ShouldThrowException() {
        when(dniValidationService.isValidDni("12345678")).thenReturn(true);
        when(userPersistencePort.findByDni("12345678")).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> userUseCase.getUserByDni("12345678"));
    }

    @Test
    void getUsersByName_Success() {
        UserModel user = new UserModel();
        user.setName("Juan");
        when(userPersistencePort.findByName("Juan")).thenReturn(List.of(user));

        List<UserModel> result = userUseCase.getUsersByName("Juan");

        assertFalse(result.isEmpty());
        assertEquals("Juan", result.get(0).getName());
    }

    @Test
    void getUsersByName_Empty_ShouldThrowException() {
        when(userPersistencePort.findByName("Nonexistent")).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> userUseCase.getUsersByName("Nonexistent"));
    }

    @Test
    void getAllUsers_Success() {
        UserModel user = new UserModel();
        when(userPersistencePort.findAll()).thenReturn(List.of(user));

        List<UserModel> result = userUseCase.getAllUsers();

        assertFalse(result.isEmpty());
    }

    @Test
    void getAllUsers_Empty_ShouldThrowException() {
        when(userPersistencePort.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> userUseCase.getAllUsers());
    }

    @Test
    void updateUser_Success() {
        UserModel existingUser = new UserModel();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@test.com");
        existingUser.setPhone("1234567890");

        UserModel updateRequest = new UserModel();
        updateRequest.setName("New Name");
        updateRequest.setEmail("new@test.com");
        updateRequest.setPhone("0987654321");

        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(existingUser));
        when(emailValidationService.isValidEmail("new@test.com")).thenReturn(true);
        when(phoneValidationService.isValidPhone("0987654321")).thenReturn(true);
        when(userPersistencePort.update(any())).thenReturn(existingUser);

        UserModel result = userUseCase.updateUser(updateRequest, 1L);

        assertNotNull(result);
        assertEquals("New Name", existingUser.getName());
        assertEquals("new@test.com", existingUser.getEmail());
        assertEquals("0987654321", existingUser.getPhone());
        verify(userPersistencePort).update(existingUser);
    }

    @Test
    void updateUser_InvalidEmail_ShouldThrowException() {
        UserModel existingUser = new UserModel();
        UserModel updateRequest = new UserModel();
        updateRequest.setEmail("invalid");

        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(existingUser));
        when(emailValidationService.isValidEmail("invalid")).thenReturn(false);

        assertThrows(ValidationException.class, () -> userUseCase.updateUser(updateRequest, 1L));
    }

    @Test
    void deleteUser_Success() {
        UserModel user = new UserModel();
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(user));

        userUseCase.deleteUser(1L);

        verify(userPersistencePort).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound_ShouldThrowException() {
        when(userPersistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> userUseCase.deleteUser(1L));
        verify(userPersistencePort, never()).deleteById(any());
    }
}
