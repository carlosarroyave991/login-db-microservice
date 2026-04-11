package microservice.user.reactive.login.domain.ports.out;

import microservice.user.reactive.login.domain.models.RefreshTokenModel;
import microservice.user.reactive.login.domain.models.UserModel;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita para la persistencia de autenticación.
 */
public interface AuthPersistencePort {
    Optional<UserModel> findByEmail(String email);
    UserModel save(UserModel user);
    void updateLastLogin(Long userId, OffsetDateTime lastLogin);
    RefreshTokenModel saveRefreshToken(RefreshTokenModel refreshToken);
    Optional<RefreshTokenModel> findByTokenHash(String tokenHash);
    boolean revokeTokenByHash(String tokenHash);
    boolean revokeAllTokensByUserId(Long userId);
}
