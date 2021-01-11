package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.db.InMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.user.UserFacade;

@Configuration
@AllArgsConstructor
class ScheduledSpringConfiguration {

    private final ScheduledSpringRepository scheduledSpringRepository;
    private final UserFacade userFacade;

    private static ScheduledFacade getScheduledFacade(UserFacade userFacade,
                                                      RepositoryAdapter<ScheduledResultsEntity> repository) {
        var modelMapper = new ModelMapper();
        var dao = new ScheduledDao(modelMapper, repository, GlobalFailureResponse.NOT_FOUND);
        return new ScheduledFacade(dao, userFacade);
    }

    public static ScheduledFacade getInMemoryScheduledFacade(UserFacade userFacade,
                                                             InMemoryRepositoryAdapter<ScheduledResultsEntity> repositoryAdapter) {
        return getScheduledFacade(userFacade, repositoryAdapter);
    }

    @Bean
    public ScheduledFacade getScheduledFacade() {
        var scheduledRepositoryAdapter = new ScheduledSpringRepositoryAdapter(scheduledSpringRepository);
        return getScheduledFacade(userFacade, scheduledRepositoryAdapter);
    }

}
