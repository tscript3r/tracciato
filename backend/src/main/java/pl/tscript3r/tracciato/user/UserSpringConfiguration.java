package pl.tscript3r.tracciato.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@RequiredArgsConstructor
public class UserSpringConfiguration {

    private final UserSpringRepository userSpringRepository;

    public static UserFacade getInMemoryUserFacade(UserRepositoryAdapter userRepositoryAdapter) {
        return get(userRepositoryAdapter);
    }

    @Bean
    public UserFacade getUserFacade() {
        return get(new UserSpringRepositoryAdapter(userSpringRepository));
    }

    private static UserFacade get(UserRepositoryAdapter userRepositoryAdapter) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        PasswordEncrypt passwordEncoder = new BCryptPasswordEncrypt();
        UserValidator userValidator = new UserValidator(validator, userRepositoryAdapter);
        UserDao userDao = new UserDao(new ModelMapper(), userRepositoryAdapter, UserFailureResponse.idNotFound());
        UserRegistration userRegistration = new UserRegistration(userValidator, passwordEncoder, userDao);
        UserAuthentication userAuthentication = new UserAuthentication(passwordEncoder, userDao);
        JWTTokenResolver jwtTokenResolver = new JWTTokenResolver();
        UserResourceAuthorization userResourceAuthorization = new UserResourceAuthorization();
        return new UserFacade(userRegistration, userAuthentication, jwtTokenResolver, userResourceAuthorization, userDao);
    }

}
