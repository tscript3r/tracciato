package pl.tscript3r.tracciato.user;

import lombok.RequiredArgsConstructor;
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
        UserRegistration userRegistration = new UserRegistration(userRepositoryAdapter, userValidator, passwordEncoder);
        UserAuthentication userAuthentication = new UserAuthentication(userRepositoryAdapter, passwordEncoder);
        JWTTokenResolver jwtTokenResolver = new JWTTokenResolver();
        return new UserFacade(userRegistration, userAuthentication, jwtTokenResolver);
    }

}
