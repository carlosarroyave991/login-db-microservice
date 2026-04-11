package microservice.user.reactive.login.infraestructure.driver.rest.mapper;

import microservice.user.reactive.login.domain.models.RoleModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.UserUpdateRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserModel toUserModel(UserUpdateRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleModelsToStrings")
    UserResponse toUserResponse(UserModel model);

    @Named("roleModelsToStrings")
    default List<String> roleModelsToStrings(List<RoleModel> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .filter(r -> r != null && r.getName() != null)
                .map(r -> r.getName().name())
                .toList();
    }
}
