package pl.tscript3r.tracciato.location;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
class LocationSpringRepositoryAdapter implements LocationRepositoryAdapter {

    private final LocationSpringRepository locationSpringRepository;

    @Override
    public Set<LocationEntity> findAllFromUser(UUID ownerUuid) {
        return locationSpringRepository.findAllByOwnerUuid(ownerUuid);
    }

    @Override
    public Option<LocationEntity> findById(Long id) {
        return Option.ofOptional(locationSpringRepository.findById(id));
    }

    @Override
    public LocationEntity save(LocationEntity entity) {
        return locationSpringRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        locationSpringRepository.deleteById(id);
    }

    @Override
    public Option<LocationEntity> findByUuid(UUID uuid) {
        return Option.of(locationSpringRepository.findByUuid(uuid));
    }

}
