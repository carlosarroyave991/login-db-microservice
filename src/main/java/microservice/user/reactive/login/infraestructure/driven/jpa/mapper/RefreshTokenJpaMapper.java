package microservice.user.reactive.login.infraestructure.driven.jpa.mapper;

import microservice.user.reactive.login.domain.models.RefreshTokenModel;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefreshTokenJpaMapper {

    @Mapping(target = "userId", source = "user.id")
    RefreshTokenModel toModel(RefreshTokenEntity entity);
}
