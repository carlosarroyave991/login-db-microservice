package microservice.user.reactive.login.infraestructure.driver.rest.mapper;

import microservice.user.reactive.login.domain.models.AuthModel;
import microservice.user.reactive.login.domain.models.RoleModel;
import microservice.user.reactive.login.domain.models.TokenModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.models.enums.RoleType;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.LoginRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.RegisterRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.TokenResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthRestMapper {

    AuthModel toAuthModel(LoginRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "stringsToRoleModels")
    UserModel toUserModel(RegisterRequest request);

    TokenResponse toTokenResponse(TokenModel model);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleModelsToStrings")
    UserResponse toUserResponse(UserModel model);

    @Named("stringsToRoleModels")
    default List<RoleModel> stringsToRoleModels(List<String> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream()
                .filter(r -> r != null && !r.isBlank())
                .map(r -> r.trim().toLowerCase())
                .map(r -> {
                    try { return RoleType.valueOf(r); } catch (IllegalArgumentException e) { return null; }
                })
                .filter(rt -> rt != null)
                .map(rt -> new RoleModel(null, rt))
                .toList();
    }

    @Named("roleModelsToStrings")
    default List<String> roleModelsToStrings(List<RoleModel> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .filter(r -> r != null && r.getName() != null)
                .map(r -> r.getName().name())
                .toList();
    }
}
