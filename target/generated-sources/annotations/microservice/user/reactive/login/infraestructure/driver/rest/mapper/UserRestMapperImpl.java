package microservice.user.reactive.login.infraestructure.driver.rest.mapper;

import javax.annotation.processing.Generated;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.UserUpdateRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T17:14:07-0500",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class UserRestMapperImpl implements UserRestMapper {

    @Override
    public UserModel toUserModel(UserUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setEmail( request.getEmail() );
        userModel.setName( request.getName() );
        userModel.setLastname( request.getLastname() );
        userModel.setPhone( request.getPhone() );
        userModel.setDni( request.getDni() );

        return userModel;
    }

    @Override
    public UserResponse toUserResponse(UserModel model) {
        if ( model == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setRoles( roleModelsToStrings( model.getRoles() ) );
        userResponse.setId( model.getId() );
        userResponse.setEmail( model.getEmail() );
        userResponse.setName( model.getName() );
        userResponse.setLastname( model.getLastname() );
        userResponse.setPhone( model.getPhone() );
        userResponse.setDni( model.getDni() );
        userResponse.setIsActive( model.getIsActive() );
        userResponse.setCreatedAt( model.getCreatedAt() );
        userResponse.setLastLogin( model.getLastLogin() );

        return userResponse;
    }
}
