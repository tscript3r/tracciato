package pl.tscript3r.tracciato.route.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pl.tscript3r.tracciato.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.TrafficPrediction;
import pl.tscript3r.tracciato.stop.StopDto;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class RouteDto {

    @JsonIgnore
    private Long id;
    private UUID uuid;
    private UUID ownerUuid;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime maxEndDate;
    private LocalDateTime creationTimestamp;
    private TrafficPrediction trafficPrediction;
    private List<AvailabilityDto> availabilities = new ArrayList<>();
    private LocationDto startLocation;
    private LocationDto endLocation;
    private Set<StopDto> stops = new HashSet<>();

}
