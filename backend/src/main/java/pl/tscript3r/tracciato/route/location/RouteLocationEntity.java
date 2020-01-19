package pl.tscript3r.tracciato.route.location;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.route.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.location.LocationEntity;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class RouteLocationEntity extends AbstractEntity {

    private UUID ownerUuid;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LocationEntity location;

    @Enumerated(EnumType.STRING)
    private LocationPriority priority;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // TODO refactor
    private List<AvailabilityEntity> availability;

    private Duration onsideDuration;

}
