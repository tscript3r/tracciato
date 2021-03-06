package pl.tscript3r.tracciato.duration;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;
import pl.tscript3r.tracciato.location.LocationEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.Instant;

@Entity
@Getter
@Setter
public class DurationEntity extends AbstractEntity {

    @OneToOne
    private LocationEntity from;

    @OneToOne
    private LocationEntity to;

    private Instant duration;

}