package microservice.user.reactive.login.infraestructure.driven.jpa.mapper;

import microservice.user.reactive.login.domain.models.RoleModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.domain.models.enums.RoleType;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserEntity;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserJpaMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(entity.getRoles()))")
    UserModel toModel(UserEntity entity);

    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserModel model);

    default List<RoleModel> mapRoles(Set<UserRoleEntity> userRoles) {
        if (userRoles == null || userRoles.isEmpty()) return List.of();
        return userRoles.stream()
                .filter(ur -> ur.getRole() != null)
                .map(ur -> new RoleModel(ur.getRole().getId(), RoleType.valueOf(ur.getRole().getName().toLowerCase())))
                .collect(Collectors.toList());
    }
}
