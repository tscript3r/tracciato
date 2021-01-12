package pl.tscript3r.tracciato.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.db.InMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.user.UserFacade;

@Configuration
@AllArgsConstructor
class ScheduledSpringConfiguration {

    private final ScheduledSpringRepository scheduledSpringRepository;
    private final UserFacade userFacade;
    private final RouteFacade routeFacade;

    private static ScheduledFacade getScheduledFacade(UserFacade userFacade,
                                                      RepositoryAdapter<ScheduledResultsEntity> repository,
                                                      RouteFacade routeFacade,
                                                      ObjectMapper mapper) {
        var modelMapper = new ModelMapper();
        var dao = new ScheduledDao(modelMapper, repository, GlobalFailureResponse.NOT_FOUND);
        var entityMapper = new SimulationsResults2Entity(mapper);
        return new ScheduledFacade(dao, userFacade, routeFacade, entityMapper);
    }

    public static ScheduledFacade getInMemoryScheduledFacade(UserFacade userFacade,
                                                             InMemoryRepositoryAdapter<ScheduledResultsEntity> repositoryAdapter,
                                                             RouteFacade routeFacade,
                                                             ObjectMapper mapper) {
        return getScheduledFacade(userFacade, repositoryAdapter, routeFacade, mapper);
    }

    @Bean
    public ScheduledFacade getScheduledFacade(ObjectMapper mapper) {
        var scheduledRepositoryAdapter = new ScheduledSpringRepositoryAdapter(scheduledSpringRepository);
        return getScheduledFacade(userFacade, scheduledRepositoryAdapter, routeFacade, mapper);
    }

}
