package pl.tscript3r.tracciato.route;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.tscript3r.tracciato.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;
import pl.tscript3r.tracciato.location.LocationEntity;
import pl.tscript3r.tracciato.stop.StopEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
class RouteEntity extends AbstractEntity {

    private UUID ownerUuid;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime maxEndDate;
    private Boolean scheduled;
    private LocalDateTime lastSchedule;
    private LocalDateTime lastUpdate;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @Enumerated(EnumType.STRING)
    private TrafficPrediction trafficPrediction;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AvailabilityEntity> availabilities = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private LocationEntity startLocation;

    @OneToOne(cascade = CascadeType.ALL)
    private LocationEntity endLocation;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<StopEntity> stops = new HashSet<>();

}
