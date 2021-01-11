package pl.tscript3r.tracciato.schedule.optimization.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ScheduleRequestDto {

    private UUID requestUuid;
    private UUID routeUuid;
    @JsonIgnore
    private UUID ownerUuid;
    private final LocalDateTime creationTimestamp = LocalDateTime.now();

}
