package microservice.user.reactive.login.infraestructure.driver.rest.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TokenRequest {
    private String token;
}
