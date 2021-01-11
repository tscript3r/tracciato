package pl.tscript3r.tracciato.scheduled;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledResultsDto {

    private UUID uuid;
    @JsonIgnore
    private UUID ownerUuid;
    private UUID requestUuid;
    private UUID routeUuid;
    private LocalDateTime creationTimestamp;
    private ScheduledVariationDto tuned;
    private ScheduledVariationDto optimal;


}
