package microservice.user.reactive.login.infraestructure.driver.rest.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserUpdateRequest {
    private String email;
    private String name;
    private String lastname;
    private String phone;
    private String dni;
    private Boolean isActive;
}
