package pl.tscript3r.tracciato.route.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import pl.tscript3r.tracciato.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.TrafficPrediction;
import pl.tscript3r.tracciato.stop.StopDto;

import java.time.LocalDateTime;
import java.util.*;

import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.DATE_TIME_FORMAT;
import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.DATE_TIME_FORMAT_WITH_SEC;

@Data
public class RouteDto {

    @JsonIgnore
    private Long id;
    private UUID uuid;
    @JsonIgnore
    private UUID ownerUuid;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime maxEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT_WITH_SEC)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationTimestamp;

    private TrafficPrediction trafficPrediction;
    private List<AvailabilityDto> availabilities = new ArrayList<>();
    private LocationDto startLocation;
    private LocationDto endLocation;
    private Set<StopDto> stops = new HashSet<>();
    private Boolean scheduled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT_WITH_SEC)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastSchedule;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT_WITH_SEC)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastUpdate;

}
