package microservice.user.reactive.login.infraestructure.driver.rest.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String lastname;
    private String phone;
    private String dni;
    private List<String> roles;
}
