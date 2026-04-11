package microservice.user.reactive.login.application;

import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.exception.DuplicateResourceException;
import microservice.user.reactive.login.domain.exception.ValidationException;
import microservice.user.reactive.login.domain.models.AuthModel;
import microservice.user.reactive.login.domain.models.RefreshTokenModel;
import microservice.user.reactive.login.domain.models.TokenModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.ports.in.IAuthPortUseCase;
import microservice.user.reactive.login.domain.ports.out.AuthPersistencePort;
import microservice.user.reactive.login.domain.service.*;
import microservice.user.reactive.login.infraestructure.config.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import static microservice.user.reactive.login.domain.exception.error.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements IAuthPortUseCase {

    private final AuthPersistencePort authPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailValidationService emailValidationService;
    private final PhoneValidationService phoneValidationService;
    private final DniValidationService dniValidationService;
    private final UserTypeValidationService userTypeValidationService;
    private final PasswordValidationService passwordValidationService;

    @Value("${application.security.jwt.expiration}")
    private Long accessTokenExpirationMs;

    @Value("${application.security.jwt.refresh-token}")
    private Long refreshTokenExpirationMs;

    @Override
    public TokenModel authenticateUser(AuthModel authModel) {
        UserModel user = authPersistencePort.findByEmail(authModel.getEmail())
                .filter(u -> passwordEncoder.matches(authModel.getPassword(), u.getPassword()))
                .orElseThrow(() -> new ValidationException(INVALID_CREDENTIALS));

        String refreshToken = UUID.randomUUID().toString();

        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name().toUpperCase()))
                .collect(Collectors.toList());

        User userDetails = new User(user.getEmail(), user.getPassword(), authorities);
        String accessToken = jwtUtil.generateToken(userDetails, refreshToken);

        RefreshTokenModel rtModel = new RefreshTokenModel();
        rtModel.setUserId(user.getId());
        rtModel.setTokenHash(refreshToken);
        rtModel.setExpiresAt(OffsetDateTime.now().plus(Duration.ofMillis(refreshTokenExpirationMs)));
        rtModel.setRevoked(false);
        rtModel.setCreatedAt(OffsetDateTime.now());

        authPersistencePort.saveRefreshToken(rtModel);
        authPersistencePort.updateLastLogin(user.getId(), OffsetDateTime.now());

        return TokenModel.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenExpirationMs / 1000)
                .build();
    }

    @Override
    public UserModel register(UserModel userModel) {
        if (!emailValidationService.isValidEmail(userModel.getEmail()))
            throw new ValidationException(INVALID_EMAIL);
        if (!phoneValidationService.isValidPhone(userModel.getPhone()))
            throw new ValidationException(INVALID_PHONE);
        if (!dniValidationService.isValidDni(userModel.getDni()))
            throw new ValidationException(INVALID_DNI);
        if (!passwordValidationService.isValidPassword(userModel.getPassword()))
            throw new ValidationException(INVALID_PASSWORD);
        if (!userTypeValidationService.isValidUserType(userModel.getRoles()))
            throw new ValidationException(INVALID_ROLE);

        authPersistencePort.findByEmail(userModel.getEmail())
                .ifPresent(u -> { throw new DuplicateResourceException(EMAIL_ALREADY_EXISTS); });

        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setIsActive(true);
        userModel.setCreatedAt(OffsetDateTime.now());

        return authPersistencePort.save(userModel);
    }

    @Override
    public boolean revokeToken(String token) {
        if (token.contains(".")) {
            try {
                String sessionId = jwtUtil.getSessionIdFromToken(token);
                return authPersistencePort.revokeTokenByHash(sessionId);
            } catch (Exception e) {
                return false;
            }
        }
        return authPersistencePort.revokeTokenByHash(token);
    }

    @Override
    public boolean validateToken(String token) {
        if (token.contains(".")) {
            if (!jwtUtil.validateToken(token)) return false;
            try {
                String sessionId = jwtUtil.getSessionIdFromToken(token);
                return authPersistencePort.findByTokenHash(sessionId)
                        .map(t -> !t.getRevoked() && t.getExpiresAt().isAfter(OffsetDateTime.now()))
                        .orElse(false);
            } catch (Exception e) {
                return false;
            }
        }
        return authPersistencePort.findByTokenHash(token)
                .map(t -> !t.getRevoked() && t.getExpiresAt().isAfter(OffsetDateTime.now()))
                .orElse(false);
    }
}
