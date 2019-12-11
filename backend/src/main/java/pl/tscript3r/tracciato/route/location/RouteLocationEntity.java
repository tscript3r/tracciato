package pl.tscript3r.tracciato.route.location;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.route.day.DayEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
public class RouteLocationEntity extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LocationEntity location;

    @Enumerated(EnumType.STRING)
    private LocationPriority priority;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // TODO refactor
    private List<DayEntity> availability;

    private Instant duration;

}
