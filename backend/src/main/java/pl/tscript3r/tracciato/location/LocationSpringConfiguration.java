package pl.tscript3r.tracciato.location;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.validation.Validation;

@Configuration
@RequiredArgsConstructor
public class LocationSpringConfiguration {

    private final UserFacade userFacade;
    private final LocationSpringRepository locationSpringRepository;

    @Bean
    public LocationFacade getLocationFacade() {
        var locationSpringRepositoryAdapter = new LocationSpringRepositoryAdapter(locationSpringRepository);
        return get(userFacade, locationSpringRepositoryAdapter);
    }

    private static LocationFacade get(UserFacade userFacade, LocationRepositoryAdapter locationRepositoryAdapter) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var locationValidator = new DefaultValidator<LocationDto>(validator);
        var locationDao = new LocationDao(new ModelMapper(), locationRepositoryAdapter, LocationFailureResponse.uuidNotFound());
        var locationFactory = new LocationFactory(locationDao, locationValidator);
        return new LocationFacade(userFacade, locationFactory, locationDao);
    }

    public static LocationFacade getInMemoryLocationFacade(UserFacade userFacade,
                                                           LocationRepositoryAdapter locationRepositoryAdapter) {
        return get(userFacade, locationRepositoryAdapter);
    }

}
