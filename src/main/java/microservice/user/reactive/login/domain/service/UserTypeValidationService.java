package microservice.user.reactive.login.domain.service;

import microservice.user.reactive.login.domain.models.RoleModel;
import microservice.user.reactive.login.domain.models.enums.RoleType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserTypeValidationService {
    public boolean isValidUserType(List<RoleModel> roles) {
        if (roles == null || roles.isEmpty()) return false;
        return roles.stream()
                .allMatch(role -> role != null
                        && role.getName() != null
                        && Arrays.stream(RoleType.values()).anyMatch(v -> v == role.getName()));
    }
}
