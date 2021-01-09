package pl.tscript3r.tracciato.scheduled;

import pl.tscript3r.tracciato.infrastructure.db.SpringRepositoryAdapter;


public class ScheduledRepositoryAdapter extends SpringRepositoryAdapter<ScheduledResultsEntity> {

    public ScheduledRepositoryAdapter(ScheduledSpringRepository springRepository) {
        super(springRepository);
    }

}
