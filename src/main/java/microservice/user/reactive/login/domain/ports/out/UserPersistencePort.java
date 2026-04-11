package microservice.user.reactive.login.domain.ports.out;

import microservice.user.reactive.login.domain.models.UserModel;

import java.util.List;
import java.util.Optional;

/**
 * Se encarga de definir las dependencias externas que el nucleo necesita.
 */
public interface UserPersistencePort {
    Optional<UserModel> findById(Long id);
    Optional<UserModel> findByDni(String dni);
    List<UserModel> findByName(String name);
    List<UserModel> findAll();
    UserModel update(UserModel user);
    void deleteById(Long id);
}
