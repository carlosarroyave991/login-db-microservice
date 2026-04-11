package microservice.user.reactive.login.application;

import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.exception.ValidationException;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.ports.in.IUserPortUseCase;
import microservice.user.reactive.login.domain.ports.out.UserPersistencePort;
import microservice.user.reactive.login.domain.service.DniValidationService;
import microservice.user.reactive.login.domain.service.EmailValidationService;
import microservice.user.reactive.login.domain.service.PhoneValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

import static microservice.user.reactive.login.domain.exception.error.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserUseCaseImpl implements IUserPortUseCase {

    private final UserPersistencePort userPersistencePort;
    private final EmailValidationService emailValidationService;
    private final PhoneValidationService phoneValidationService;
    private final DniValidationService dniValidationService;

    @Override
    public UserModel getUserById(Long id) {
        return userPersistencePort.findById(id)
                .orElseThrow(() -> new ValidationException(USER_NOT_FOUND));
    }

    @Override
    public UserModel getUserByDni(String dni) {
        if (!dniValidationService.isValidDni(dni))
            throw new ValidationException(INVALID_DNI);
        return userPersistencePort.findByDni(dni)
                .orElseThrow(() -> new ValidationException(DNI_NOT_FOUND));
    }

    @Override
    public List<UserModel> getUsersByName(String name) {
        List<UserModel> result = userPersistencePort.findByName(name);
        if (result.isEmpty()) throw new ValidationException(USER_NAME_NOT_FOUND);
        return result;
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserModel> result = userPersistencePort.findAll();
        if (result.isEmpty()) throw new ValidationException(DB_EMPTY);
        return result;
    }

    @Override
    public UserModel updateUser(UserModel user, Long id) {
        UserModel existing = userPersistencePort.findById(id)
                .orElseThrow(() -> new ValidationException(USER_NOT_FOUND));

        if (user.getEmail() != null && !emailValidationService.isValidEmail(user.getEmail()))
            throw new ValidationException(INVALID_EMAIL);
        if (user.getPhone() != null && !phoneValidationService.isValidPhone(user.getPhone()))
            throw new ValidationException(INVALID_PHONE);

        if (user.getName() != null) existing.setName(user.getName());
        if (user.getLastname() != null) existing.setLastname(user.getLastname());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getPhone() != null) existing.setPhone(user.getPhone());
        if (user.getDni() != null) existing.setDni(user.getDni());
        if (user.getIsActive() != null) existing.setIsActive(user.getIsActive());

        return userPersistencePort.update(existing);
    }

    @Override
    public void deleteUser(Long id) {
        userPersistencePort.findById(id)
                .orElseThrow(() -> new ValidationException(USER_NOT_FOUND));
        userPersistencePort.deleteById(id);
    }
}
