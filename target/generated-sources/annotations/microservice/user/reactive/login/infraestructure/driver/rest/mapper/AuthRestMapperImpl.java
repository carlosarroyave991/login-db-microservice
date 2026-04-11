package microservice.user.reactive.login.infraestructure.driver.rest.mapper;

import javax.annotation.processing.Generated;
import microservice.user.reactive.login.domain.models.AuthModel;
import microservice.user.reactive.login.domain.models.TokenModel;
import microservice.user.reactive.login.domain.models.UserModel;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.LoginRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.req.RegisterRequest;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.TokenResponse;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.UserResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-11T16:58:30-0500",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class AuthRestMapperImpl implements AuthRestMapper {

    @Override
    public AuthModel toAuthModel(LoginRequest request) {
        if ( request == null ) {
            return null;
        }

        AuthModel authModel = new AuthModel();

        authModel.setEmail( request.getEmail() );
        authModel.setPassword( request.getPassword() );

        return authModel;
    }

    @Override
    public UserModel toUserModel(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setRoles( stringsToRoleModels( request.getRoles() ) );
        userModel.setEmail( request.getEmail() );
        userModel.setPassword( request.getPassword() );
        userModel.setName( request.getName() );
        userModel.setLastname( request.getLastname() );
        userModel.setPhone( request.getPhone() );
        userModel.setDni( request.getDni() );

        return userModel;
    }

    @Override
    public TokenResponse toTokenResponse(TokenModel model) {
        if ( model == null ) {
            return null;
        }

        TokenResponse.TokenResponseBuilder tokenResponse = TokenResponse.builder();

        tokenResponse.accessToken( model.getAccessToken() );
        tokenResponse.refreshToken( model.getRefreshToken() );
        tokenResponse.expiresIn( model.getExpiresIn() );

        return tokenResponse.build();
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
