package microservice.user.reactive.login.domain.service;

import microservice.user.reactive.login.domain.config.Const;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidationService {
    public boolean isValidPhone(String phone) {
        return phone != null && Const.PATTERN_PHONE.matcher(phone).matches();
    }
}
