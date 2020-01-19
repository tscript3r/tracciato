package pl.tscript3r.tracciato.route;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.tscript3r.tracciato.route.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.location.LocationEntity;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
public class RouteEntity extends AbstractEntity {

    private UUID uuid;
    private UUID ownerUuid;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime maxEndDate;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @Enumerated(EnumType.STRING)
    private TrafficPrediction trafficPrediction;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AvailabilityEntity> availabilities = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LocationEntity startLocation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LocationEntity endLocation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RouteLocationEntity> locations = new HashSet<>();

}
