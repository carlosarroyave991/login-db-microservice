package microservice.user.reactive.login.domain.ports.in;

import microservice.user.reactive.login.domain.models.UserModel;

import java.util.List;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface IUserPortUseCase {
    UserModel getUserById(Long id);
    UserModel getUserByDni(String dni);
    List<UserModel> getUsersByName(String name);
    List<UserModel> getAllUsers();
    UserModel updateUser(UserModel user, Long id);
    void deleteUser(Long id);
}
