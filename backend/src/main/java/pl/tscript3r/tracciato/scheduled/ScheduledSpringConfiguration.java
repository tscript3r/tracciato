package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;

@Configuration
@AllArgsConstructor
class ScheduledSpringConfiguration {

    private final ScheduledSpringRepository scheduledSpringRepository;

    @Bean
    public ScheduledFacade getScheduledFacade() {
        var modelMapper = new ModelMapper();
        var scheduledRepositoryAdapter = new ScheduledRepositoryAdapter(scheduledSpringRepository);
        return new ScheduledFacade(new ScheduledDao(modelMapper, scheduledRepositoryAdapter, GlobalFailureResponse.NOT_FOUND));
    }

}
