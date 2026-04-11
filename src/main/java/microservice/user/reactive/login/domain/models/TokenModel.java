package microservice.user.reactive.login.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenModel {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
