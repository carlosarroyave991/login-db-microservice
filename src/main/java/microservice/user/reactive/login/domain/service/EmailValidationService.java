package microservice.user.reactive.login.domain.service;

import microservice.user.reactive.login.domain.config.Const;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationService {
    public boolean isValidEmail(String email) {
        return email != null && Const.EMAIL_PATTERN.matcher(email).matches();
    }
}
