package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.db.InMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;

@Configuration
@AllArgsConstructor
class ScheduledSpringConfiguration {

    private final ScheduledSpringRepository scheduledSpringRepository;

    private static ScheduledFacade getScheduledFacade(RepositoryAdapter<ScheduledResultsEntity> repository) {
        var modelMapper = new ModelMapper();
        var dao = new ScheduledDao(modelMapper, repository, GlobalFailureResponse.NOT_FOUND);
        return new ScheduledFacade(dao);
    }

    public static ScheduledFacade getInMemoryScheduledFacade(
            InMemoryRepositoryAdapter<ScheduledResultsEntity> repositoryAdapter) {
        return getScheduledFacade(repositoryAdapter);
    }

    @Bean
    public ScheduledFacade getScheduledFacade() {
        var scheduledRepositoryAdapter = new ScheduledSpringRepositoryAdapter(scheduledSpringRepository);
        return getScheduledFacade(scheduledRepositoryAdapter);
    }

}
