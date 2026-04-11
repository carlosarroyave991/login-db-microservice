package microservice.user.reactive.login.domain.service;

import microservice.user.reactive.login.domain.config.Const;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationService {
    public boolean isValidPassword(String password) {
        return password != null && Const.PATTERN_PASSWORD.matcher(password).matches();
    }
}
