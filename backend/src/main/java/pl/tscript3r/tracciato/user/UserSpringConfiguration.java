package pl.tscript3r.tracciato.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class UserSpringConfiguration {

    private final UserSpringRepository userSpringRepository;

    public static UserFacade getInMemoryUserFacade() {
        PasswordEncrypt passwordEncoder = new BCryptPasswordEncrypt();
        UserRepositoryAdapter userRepositoryAdapter = new InMemoryUserRepositoryAdapter();
        UserValidator userValidator = new UserValidator(userRepositoryAdapter);
        UserRegistration userRegistration = new UserRegistration(userRepositoryAdapter, userValidator, passwordEncoder);
        return new UserFacade(userRegistration);
    }

    @Bean
    public UserFacade getUserFacade() {
        PasswordEncrypt passwordEncoder = new BCryptPasswordEncrypt();
        UserRepositoryAdapter userRepositoryAdapter = new UserSpringRepositoryAdapter(userSpringRepository);
        UserValidator userValidator = new UserValidator(userRepositoryAdapter);
        UserRegistration userRegistration = new UserRegistration(userRepositoryAdapter, userValidator, passwordEncoder);
        return new UserFacade(userRegistration);
    }

}
