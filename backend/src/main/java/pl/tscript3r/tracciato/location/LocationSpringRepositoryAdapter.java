package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.infrastructure.db.SpringRepositoryAdapter;

import java.util.Set;
import java.util.UUID;

class LocationSpringRepositoryAdapter extends SpringRepositoryAdapter<LocationEntity>
        implements LocationRepositoryAdapter {

    private final LocationSpringRepository locationSpringRepository;

    public LocationSpringRepositoryAdapter(LocationSpringRepository springRepository) {
        super(springRepository);
        this.locationSpringRepository = springRepository;
    }

    @Override
    public Set<LocationEntity> findAllFromUser(UUID ownerUuid) {
        return locationSpringRepository.findAllByOwnerUuid(ownerUuid);
    }

}
