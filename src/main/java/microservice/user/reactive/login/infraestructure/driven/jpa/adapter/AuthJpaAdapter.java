package microservice.user.reactive.login.infraestructure.driven.jpa.adapter;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.models.RefreshTokenModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.models.RoleModel;
import microservice.user.reactive.login.domain.models.enums.RoleType;
import microservice.user.reactive.login.domain.ports.out.AuthPersistencePort;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.RefreshTokenEntity;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.RoleEntity;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserEntity;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserRoleEntity;
import microservice.user.reactive.login.infraestructure.driven.jpa.repository.UserRoleJpaRepository;
import microservice.user.reactive.login.infraestructure.driven.jpa.mapper.RefreshTokenJpaMapper;
import microservice.user.reactive.login.infraestructure.driven.jpa.mapper.UserJpaMapper;
import microservice.user.reactive.login.infraestructure.driven.jpa.repository.RefreshTokenJpaRepository;
import microservice.user.reactive.login.infraestructure.driven.jpa.repository.RoleJpaRepository;
import microservice.user.reactive.login.infraestructure.driven.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthJpaAdapter implements AuthPersistencePort {

    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final UserRoleJpaRepository userRoleRepository;
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final UserJpaMapper userMapper;
    private final RefreshTokenJpaMapper refreshTokenMapper;

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toModel);
    }

    @Override
    @Transactional
    public UserModel save(UserModel user) {
        UserEntity entity = userMapper.toEntity(user);

        RoleType roleType = (user.getRoles() == null || user.getRoles().isEmpty())
                ? RoleType.user
                : user.getRoles().get(0).getName();

        RoleEntity role = roleRepository.findByName(roleType.name())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleType.name()));

        UserEntity savedUser = userRepository.save(entity);

        UserRoleEntity userRole = new UserRoleEntity(null, savedUser, role);
        userRoleRepository.save(userRole);

        // Construimos el modelo de retorno directamente con el rol ya conocido
        // para evitar depender del caché de primer nivel de Hibernate
        UserModel result = userMapper.toModel(savedUser);
        result.setRoles(List.of(new RoleModel(role.getId(), RoleType.valueOf(role.getName().toLowerCase()))));
        return result;
    }

    @Override
    @Transactional
    public void updateLastLogin(Long userId, OffsetDateTime lastLogin) {
        userRepository.updateLastLogin(userId, lastLogin);
    }

    @Override
    @Transactional
    public RefreshTokenModel saveRefreshToken(RefreshTokenModel model) {
        UserEntity user = userRepository.findById(model.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + model.getUserId()));

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUser(user);
        entity.setTokenHash(model.getTokenHash());
        entity.setExpiresAt(model.getExpiresAt());
        entity.setRevoked(model.getRevoked());
        entity.setCreatedAt(model.getCreatedAt());

        return refreshTokenMapper.toModel(refreshTokenRepository.save(entity));
    }

    @Override
    public Optional<RefreshTokenModel> findByTokenHash(String tokenHash) {
        return refreshTokenRepository.findByTokenHash(tokenHash).map(refreshTokenMapper::toModel);
    }

    @Override
    @Transactional
    public boolean revokeTokenByHash(String tokenHash) {
        return refreshTokenRepository.revokeByTokenHash(tokenHash) > 0;
    }

    @Override
    @Transactional
    public boolean revokeAllTokensByUserId(Long userId) {
        return refreshTokenRepository.revokeAllByUserId(userId) > 0;
    }
}
