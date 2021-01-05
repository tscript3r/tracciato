package pl.tscript3r.tracciato.route.availability;

import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;

import java.time.LocalTime;

public final class AvailabilityConst {

    private AvailabilityConst() {
    }

    public static AvailabilityDto getValidAvailabilityDto() {
        var results = new AvailabilityDto();
        results.setDate(RouteConst.START_DATE.plusDays(1).toLocalDate());
        results.setTo(LocalTime.of(16, 0));
        results.setFrom(LocalTime.of(8, 0));
        return results;
    }

    public static AvailabilityEntity getValidAvailabilityEntity() {
        var results = new AvailabilityEntity();
        results.setDate(RouteConst.START_DATE.plusDays(1).toLocalDate());
        results.setTo(LocalTime.of(16, 0));
        results.setFrom(LocalTime.of(8, 0));
        return results;
    }

}
