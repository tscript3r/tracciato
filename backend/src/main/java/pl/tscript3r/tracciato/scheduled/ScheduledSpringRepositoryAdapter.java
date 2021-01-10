package pl.tscript3r.tracciato.scheduled;

import pl.tscript3r.tracciato.infrastructure.db.SpringRepositoryAdapter;


class ScheduledSpringRepositoryAdapter extends SpringRepositoryAdapter<ScheduledResultsEntity> {

    public ScheduledSpringRepositoryAdapter(ScheduledSpringRepository springRepository) {
        super(springRepository);
    }

}
