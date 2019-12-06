package pl.tscript3r.tracciato.route;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.route.day.DayEntity;
import pl.tscript3r.tracciato.route.location.LocationEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class RouteEntity extends AbstractEntity {

    private UUID uuid;
    private LocalDateTime startDate;
    private LocalDateTime maxEndDate;

    @Enumerated(EnumType.STRING)
    private TrafficPrediction trafficPrediction;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DayEntity> days;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<LocationEntity> locations;

}
