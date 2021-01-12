package pl.tscript3r.tracciato.scheduled;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.DATE_TIME_FORMAT_WITH_SEC;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledVariationDto {

    private List<UUID> order;
    private List<Map<String, String>> timeline;
    private List<UUID> missedAppointments;
    private Long travelledMeters;
    private Integer missedAppointmentsCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT_WITH_SEC)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endingDate;

}
