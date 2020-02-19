package pl.tscript3r.tracciato.duration.provider.google;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import pl.tscript3r.tracciato.duration.provider.DurationDto;

import java.time.Duration;

final class DirectionsResult2DurationDto {

    static DurationDto map(DirectionsResult directionsResult) {
        DirectionsLeg leg = findQuickestDirectionLeg(directionsResult);
        Duration duration = Duration.ofSeconds(leg.duration.inSeconds);
        return new DurationDto(duration, leg.distance.inMeters);
    }

    private static DirectionsLeg findQuickestDirectionLeg(DirectionsResult directionsResult) {
        DirectionsLeg quickestLeg = null;
        for (DirectionsRoute directionsRoute : directionsResult.routes)
            for (DirectionsLeg directionsLeg : directionsRoute.legs) {
                if (quickestLeg == null)
                    quickestLeg = directionsLeg;
                if (quickestLeg.duration.inSeconds > directionsLeg.duration.inSeconds)
                    quickestLeg = directionsLeg;
            }
        return quickestLeg;
    }

}
