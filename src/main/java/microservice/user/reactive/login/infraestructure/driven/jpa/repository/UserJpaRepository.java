package microservice.user.reactive.login.infraestructure.driven.jpa.repository;

import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.role WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.role WHERE u.dni = :dni")
    Optional<UserEntity> findByDni(@Param("dni") String dni);

    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.role WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<UserEntity> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCase(@Param("name") String name, @Param("name") String lastname);

    @Modifying
    @Query("UPDATE UserEntity u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    int updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") OffsetDateTime lastLogin);
}
