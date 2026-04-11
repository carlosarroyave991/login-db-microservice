package microservice.user.reactive.login.infraestructure.driven.jpa.mapper;

import javax.annotation.processing.Generated;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.infraestructure.driven.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T17:14:07-0500",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class UserJpaMapperImpl implements UserJpaMapper {

    @Override
    public UserModel toModel(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setId( entity.getId() );
        userModel.setEmail( entity.getEmail() );
        userModel.setPassword( entity.getPassword() );
        userModel.setName( entity.getName() );
        userModel.setLastname( entity.getLastname() );
        userModel.setPhone( entity.getPhone() );
        userModel.setDni( entity.getDni() );
        userModel.setIsActive( entity.getIsActive() );
        userModel.setCreatedAt( entity.getCreatedAt() );
        userModel.setLastLogin( entity.getLastLogin() );

        userModel.setRoles( mapRoles(entity.getRoles()) );

        return userModel;
    }

    @Override
    public UserEntity toEntity(UserModel model) {
        if ( model == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( model.getId() );
        userEntity.setEmail( model.getEmail() );
        userEntity.setPassword( model.getPassword() );
        userEntity.setName( model.getName() );
        userEntity.setLastname( model.getLastname() );
        userEntity.setPhone( model.getPhone() );
        userEntity.setDni( model.getDni() );
        userEntity.setIsActive( model.getIsActive() );
        userEntity.setCreatedAt( model.getCreatedAt() );
        userEntity.setLastLogin( model.getLastLogin() );

        return userEntity;
    }
}
