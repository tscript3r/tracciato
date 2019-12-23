package pl.tscript3r.tracciato.route;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.route.location.day.DayAvailabilityEntity;

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
    private List<DayAvailabilityEntity> days = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RouteLocationEntity startLocation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RouteLocationEntity endLocation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RouteLocationEntity> locations = new HashSet<>();

}
