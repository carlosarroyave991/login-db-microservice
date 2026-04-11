package microservice.user.reactive.login.infraestructure.driven.jpa.repository;

import microservice.user.reactive.login.infraestructure.driven.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
