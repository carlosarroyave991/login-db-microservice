package microservice.user.reactive.login.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenModel {
    private Long id;
    private Long userId;
    private String tokenHash;
    private OffsetDateTime expiresAt;
    private Boolean revoked;
    private OffsetDateTime createdAt;
}
