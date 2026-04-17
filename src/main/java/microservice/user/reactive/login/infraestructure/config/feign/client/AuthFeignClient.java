package microservice.user.reactive.login.infraestructure.config.feign.client;

import microservice.user.reactive.login.infraestructure.config.feign.FeignConfig;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.TokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "login-service",
        url = "${services.login.url}",
        configuration = FeignConfig.class
)
public interface AuthFeignClient {

    @PostMapping("/api/auth/validate")
    Boolean validateToken(@RequestBody TokenRequest request);
}
