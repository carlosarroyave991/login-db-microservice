package microservice.user.reactive.login.infraestructure.driven.jpa.mapper;

import javax.annotation.processing.Generated;
import microservice.user.reactive.login.domain.models.RefreshTokenModel;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.RefreshTokenEntity;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T16:58:30-0500",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class RefreshTokenJpaMapperImpl implements RefreshTokenJpaMapper {

    @Override
    public RefreshTokenModel toModel(RefreshTokenEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();

        refreshTokenModel.setUserId( entityUserId( entity ) );
        refreshTokenModel.setId( entity.getId() );
        refreshTokenModel.setTokenHash( entity.getTokenHash() );
        refreshTokenModel.setExpiresAt( entity.getExpiresAt() );
        refreshTokenModel.setRevoked( entity.getRevoked() );
        refreshTokenModel.setCreatedAt( entity.getCreatedAt() );

        return refreshTokenModel;
    }

    private Long entityUserId(RefreshTokenEntity refreshTokenEntity) {
        UserEntity user = refreshTokenEntity.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
