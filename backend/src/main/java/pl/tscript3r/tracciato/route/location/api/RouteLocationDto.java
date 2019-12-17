package pl.tscript3r.tracciato.route.location.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import pl.tscript3r.tracciato.infrastructure.validator.OneNotEmpty;
import pl.tscript3r.tracciato.route.location.LocationPriority;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.TIME_FORMAT;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@OneNotEmpty(fields = {"location", "existingLocationId"}, message = "No new / existing location given")
public class RouteLocationDto {

    @JsonIgnore
    private UUID ownerUuid;

    @Valid
    private LocationDto location;

    private Long existingLocationId;

    @NotNull
    private LocationPriority priority;

    @NotNull
    @Valid
    private List<DayAvailabilityDto> availability = new ArrayList<>();

    @NotNull
    @DateTimeFormat(pattern = TIME_FORMAT)
    private String onsideDuration;

}