package microservice.user.reactive.login.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String lastname;
    private String phone;
    private String dni;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
    private List<RoleModel> roles;
}
