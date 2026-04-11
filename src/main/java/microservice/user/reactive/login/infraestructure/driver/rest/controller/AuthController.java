package microservice.user.reactive.login.infraestructure.driver.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.ports.in.IAuthPortUseCase;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.LoginRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.RegisterRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.TokenRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.TokenResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.mapper.AuthRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para Login, Registro y gestión de Tokens")
public class AuthController {

    private final IAuthPortUseCase authUseCase;
    private final AuthRestMapper authRestMapper;

    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y devuelve los tokens JWT.")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                authRestMapper.toTokenResponse(
                        authUseCase.authenticateUser(authRestMapper.toAuthModel(request))));
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Crea un nuevo usuario en el sistema.")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                authRestMapper.toUserResponse(
                        authUseCase.register(authRestMapper.toUserModel(request))));
    }

    @PostMapping("/revoke")
    @Operation(summary = "Revocar Token", description = "Invalida un token (Logout).")
    public ResponseEntity<Boolean> revoke(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authUseCase.revokeToken(request.getToken()));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validar Token", description = "Verifica si un token es válido.")
    public ResponseEntity<Boolean> validate(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authUseCase.validateToken(request.getToken()));
    }
}
