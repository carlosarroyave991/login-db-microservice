package microservice.user.reactive.login.domain.ports.in;

import microservice.user.reactive.login.domain.models.AuthModel;
import microservice.user.reactive.login.domain.models.TokenModel;
import microservice.user.reactive.login.domain.models.UserModel;

/**
 * Se definen las operaciones REST que pueden utilizar para interactuar
 * con el nucreo del sistema.
 */
public interface IAuthPortUseCase {
    TokenModel authenticateUser(AuthModel authModel);
    UserModel register(UserModel userModel);
    boolean revokeToken(String token);
    boolean validateToken(String token);
}
