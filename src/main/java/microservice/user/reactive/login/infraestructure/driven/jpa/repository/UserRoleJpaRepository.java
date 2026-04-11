package microservice.user.reactive.login.infraestructure.driven.jpa.repository;

import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {
}
