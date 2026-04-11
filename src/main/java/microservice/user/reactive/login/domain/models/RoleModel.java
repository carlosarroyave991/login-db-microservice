package microservice.user.reactive.login.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.user.reactive.login.domain.models.enums.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {
    private Long id;
    private RoleType name;
}
