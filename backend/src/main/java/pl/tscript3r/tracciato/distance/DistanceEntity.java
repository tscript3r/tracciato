package pl.tscript3r.tracciato.distance;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.location.LocationEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.Instant;

@Entity
@Getter
@Setter
public class DistanceEntity extends AbstractEntity {

    @OneToOne
    private LocationEntity from;

    @OneToOne
    private LocationEntity to;

    private Instant duration;

}
