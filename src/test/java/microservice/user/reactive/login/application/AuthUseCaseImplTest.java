package microservice.user.reactive.login.application;

import microservice.user.reactive.login.domain.exception.DuplicateResourceException;
import microservice.user.reactive.login.domain.exception.ValidationException;
import microservice.user.reactive.login.domain.models.AuthModel;
import microservice.user.reactive.login.domain.models.RefreshTokenModel;
import microservice.user.reactive.login.domain.models.TokenModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.ports.out.AuthPersistencePort;
import microservice.user.reactive.login.domain.service.*;
import microservice.user.reactive.login.infraestructure.config.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseImplTest {

    @Mock
    private AuthPersistencePort authPersistencePort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private EmailValidationService emailValidationService;
    @Mock
    private PhoneValidationService phoneValidationService;
    @Mock
    private DniValidationService dniValidationService;
    @Mock
    private UserTypeValidationService userTypeValidationService;
    @Mock
    private PasswordValidationService passwordValidationService;

    @InjectMocks
    private AuthUseCaseImpl authUseCase;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authUseCase, "accessTokenExpirationMs", 3600000L);
        ReflectionTestUtils.setField(authUseCase, "refreshTokenExpirationMs", 86400000L);
    }

    @Test
    void authenticateUser_Success() {
        AuthModel authModel = new AuthModel("test@test.com", "password");
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@test.com");
        userModel.setPassword("encodedPassword");
        userModel.setRoles(Collections.emptyList());

        when(authPersistencePort.findByEmail(authModel.getEmail())).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches(authModel.getPassword(), userModel.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(any(), any())).thenReturn("accessToken");

        TokenModel result = authUseCase.authenticateUser(authModel);

        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        verify(authPersistencePort).saveRefreshToken(any());
        verify(authPersistencePort).updateLastLogin(eq(1L), any());
    }

    @Test
    void authenticateUser_InvalidCredentials_ShouldThrowException() {
        AuthModel authModel = new AuthModel("test@test.com", "password");
        when(authPersistencePort.findByEmail(authModel.getEmail())).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> authUseCase.authenticateUser(authModel));
    }

    @Test
    void register_Success() {
        UserModel userModel = new UserModel();
        userModel.setEmail("test@test.com");
        userModel.setPassword("Password123");
        userModel.setPhone("1234567890");
        userModel.setDni("1020304050");

        when(emailValidationService.isValidEmail(any())).thenReturn(true);
        when(phoneValidationService.isValidPhone(any())).thenReturn(true);
        when(dniValidationService.isValidDni(any())).thenReturn(true);
        when(passwordValidationService.isValidPassword(any())).thenReturn(true);
        when(userTypeValidationService.isValidUserType(any())).thenReturn(true);
        when(authPersistencePort.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(authPersistencePort.save(any())).thenReturn(userModel);

        UserModel result = authUseCase.register(userModel);

        assertNotNull(result);
        verify(authPersistencePort).save(any());
    }

    @Test
    void register_EmailAlreadyExists_ShouldThrowException() {
        UserModel userModel = new UserModel();
        userModel.setEmail("test@test.com");

        when(emailValidationService.isValidEmail(any())).thenReturn(true);
        when(phoneValidationService.isValidPhone(any())).thenReturn(true);
        when(dniValidationService.isValidDni(any())).thenReturn(true);
        when(passwordValidationService.isValidPassword(any())).thenReturn(true);
        when(userTypeValidationService.isValidUserType(any())).thenReturn(true);
        when(authPersistencePort.findByEmail("test@test.com")).thenReturn(Optional.of(new UserModel()));

        assertThrows(DuplicateResourceException.class, () -> authUseCase.register(userModel));
    }

    @Test
    void revokeToken_Jwt_Success() {
        String token = "header.payload.signature";
        when(jwtUtil.getSessionIdFromToken(token)).thenReturn("sessionId");
        when(authPersistencePort.revokeTokenByHash("sessionId")).thenReturn(true);

        boolean result = authUseCase.revokeToken(token);

        assertTrue(result);
    }

    @Test
    void revokeToken_RefreshToken_Success() {
        String token = "uuid-refresh-token";
        when(authPersistencePort.revokeTokenByHash(token)).thenReturn(true);

        boolean result = authUseCase.revokeToken(token);

        assertTrue(result);
    }

    @Test
    void validateToken_Jwt_Valid_ShouldReturnTrue() {
        String token = "header.payload.signature";
        RefreshTokenModel rtModel = new RefreshTokenModel();
        rtModel.setRevoked(false);
        rtModel.setExpiresAt(OffsetDateTime.now().plusHours(1));

        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getSessionIdFromToken(token)).thenReturn("sessionId");
        when(authPersistencePort.findByTokenHash("sessionId")).thenReturn(Optional.of(rtModel));

        boolean result = authUseCase.validateToken(token);

        assertTrue(result);
    }

    @Test
    void validateToken_RefreshToken_Valid_ShouldReturnTrue() {
        String token = "uuid-refresh-token";
        RefreshTokenModel rtModel = new RefreshTokenModel();
        rtModel.setRevoked(false);
        rtModel.setExpiresAt(OffsetDateTime.now().plusHours(1));

        when(authPersistencePort.findByTokenHash(token)).thenReturn(Optional.of(rtModel));

        boolean result = authUseCase.validateToken(token);

        assertTrue(result);
    }
}
