package pl.tscript3r.tracciato.route;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;
import pl.tscript3r.tracciato.route.day.DayEntity;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
class RouteEntity extends AbstractEntity {

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
    private List<DayEntity> days = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    private RouteLocationEntity startLocation;

    @OneToOne(fetch = FetchType.EAGER)
    private RouteLocationEntity endLocation;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RouteLocationEntity> locations;

}
