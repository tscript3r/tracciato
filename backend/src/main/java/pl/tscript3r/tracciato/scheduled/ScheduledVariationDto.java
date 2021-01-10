package pl.tscript3r.tracciato.scheduled;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledVariationDto {

    private List<UUID> order;
    private List<Map<String, String>> timeline;
    private List<UUID> missedAppointments;
    private Long travelledMeters;
    private Integer missedAppointmentsCount;
    private LocalDateTime endingDate;

}
