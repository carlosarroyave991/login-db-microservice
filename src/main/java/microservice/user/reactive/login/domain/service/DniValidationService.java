package microservice.user.reactive.login.domain.service;

import microservice.user.reactive.login.domain.config.Const;
import org.springframework.stereotype.Component;

@Component
public class DniValidationService {
    public boolean isValidDni(String dni) {
        return dni != null && Const.PATTERN_DNI.matcher(dni).matches();
    }
}
