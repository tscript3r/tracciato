package pl.tscript3r.tracciato.availability.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.tscript3r.tracciato.infrastructure.validator.TimeBeforeAfter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.DATE_FORMAT;
import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.TIME_FORMAT;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TimeBeforeAfter(beforeField = "from", afterField = "to", switchEnableValue = true, switchField = "excluded",
        message = "Not excluded day needs to have time range set")
public class AvailabilityDto {

    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @DateTimeFormat(pattern = TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime from;

    @DateTimeFormat(pattern = TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime to;

    private Boolean excluded = false;

    public static AvailabilityDto get24hAvailabilityDay(LocalDate date) {
        var result = new AvailabilityDto();
        result.excluded = false;
        result.date = date;
        result.from = LocalTime.MIN;
        result.to = LocalTime.MIDNIGHT;
        return result;
    }

}
