package pl.tscript3r.tracciato.scheduled;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.infrastructure.db.Dao;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

class ScheduledDao extends Dao<ScheduledResultsEntity, ScheduledResultsDto> {

    ScheduledDao(ModelMapper modelMapper,
                 RepositoryAdapter<ScheduledResultsEntity> repositoryAdapter,
                 FailureResponse notFoundFailureResponse) {
        super(modelMapper, repositoryAdapter, notFoundFailureResponse);
    }

    public ScheduledResultsDto save(ScheduledResultsEntity entity) {
        var saved = repositoryAdapter.save(entity);
        return modelMapper.map(saved, ScheduledResultsDto.class);
    }

}
