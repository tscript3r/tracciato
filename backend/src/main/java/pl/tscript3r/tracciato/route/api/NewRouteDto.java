package pl.tscript3r.tracciato.route.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.tscript3r.tracciato.infrastructure.validator.TimeBeforeAfter;
import pl.tscript3r.tracciato.route.TrafficPrediction;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.DATE_TIME_FORMAT;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TimeBeforeAfter(beforeField = "startDate", afterField = "maxEndDate", message = "Start date needs to be before max end date")
public class NewRouteDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @JsonIgnore
    private UUID ownerUuid;

    @Size(min = 3, max = 255)
    private String name;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime maxEndDate;

    private TrafficPrediction traffic;

    public NewRouteDto setOwner(UUID owner) {
        this.ownerUuid = owner;
        return this;
    }

}
