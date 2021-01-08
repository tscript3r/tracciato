package pl.tscript3r.tracciato.stop;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;
import pl.tscript3r.tracciato.location.LocationEntity;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class StopEntity extends AbstractEntity {

    private UUID ownerUuid;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LocationEntity location;

    @Enumerated(EnumType.STRING)
    private StopPriority priority;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // TODO refactor
    private List<AvailabilityEntity> availability;

    private Duration onsideDuration;

}
