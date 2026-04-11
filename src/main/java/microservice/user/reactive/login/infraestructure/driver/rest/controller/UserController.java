package microservice.user.reactive.login.infraestructure.driver.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import microservice.user.reactive.login.domain.ports.in.IUserPortUseCase;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.UserUpdateRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.mapper.UserRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Gestión de Usuarios", description = "Endpoints para la administración de usuarios")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final IUserPortUseCase userUseCase;
    private final UserRestMapper userRestMapper;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados.")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userUseCase.getAllUsers().stream()
                .map(userRestMapper::toUserResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico por su ID.")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userRestMapper.toUserResponse(userUseCase.getUserById(id)));
    }

    @GetMapping("/dni/{dni}")
    @Operation(summary = "Obtener usuario por DNI", description = "Retorna un usuario específico por su DNI.")
    public ResponseEntity<UserResponse> getByDni(@PathVariable String dni) {
        return ResponseEntity.ok(userRestMapper.toUserResponse(userUseCase.getUserByDni(dni)));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar usuarios por nombre", description = "Retorna una lista de usuarios que coinciden con el nombre proporcionado.")
    public ResponseEntity<List<UserResponse>> getByName(@RequestParam String name) {
        return ResponseEntity.ok(userUseCase.getUsersByName(name).stream()
                .map(userRestMapper::toUserResponse)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente.")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(
                userRestMapper.toUserResponse(
                        userUseCase.updateUser(userRestMapper.toUserModel(request), id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
