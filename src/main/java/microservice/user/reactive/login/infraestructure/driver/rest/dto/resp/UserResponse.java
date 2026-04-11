package microservice.user.reactive.login.infraestructure.driver.rest.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String lastname;
    private String phone;
    private String dni;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
    private List<String> roles;
}
