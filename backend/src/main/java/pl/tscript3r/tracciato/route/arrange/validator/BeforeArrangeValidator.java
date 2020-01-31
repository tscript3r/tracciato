package pl.tscript3r.tracciato.route.arrange.validator;

import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.validator.BindingFailureResponse;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public final class BeforeArrangeValidator {

    private static final long MINIMAL_HOUR_ROUTE_DURATION = 5;

    public static InternalResponse<RouteDto> validate(RouteDto routeDto) {
        Map<String, String> validationResults = new HashMap<>();
        duration(routeDto, validationResults);
        availabilityDates(routeDto, validationResults);
        trafficPrediction(routeDto, validationResults);
        startEndLocation(routeDto, validationResults);
        betweenLocations(routeDto, validationResults);
        if (validationResults.size() <= 0)
            return InternalResponse.payload(routeDto);
        else
            return InternalResponse.failure(BindingFailureResponse.get(validationResults));
    }

    private static void duration(RouteDto routeDto, Map<String, String> validationResults) {
        var startDate = routeDto.getStartDate();
        var endDate = routeDto.getMaxEndDate();
        if (startDate.isAfter(endDate))
            validationResults.put("duration", "Start date is after end date");
        var routeHoursDuration = ChronoUnit.HOURS.between(startDate, endDate);
        if (routeHoursDuration <= MINIMAL_HOUR_ROUTE_DURATION)
            validationResults.put("duration", "Route maximal duration is to short (minimum " + MINIMAL_HOUR_ROUTE_DURATION + "h)");
    }

    private static void availabilityDates(RouteDto routeDto, Map<String, String> validationResults) {
        var startDate = routeDto.getStartDate();
        var endDate = routeDto.getMaxEndDate();
        for (AvailabilityDto availabilityDto : routeDto.getAvailabilities()) {
            var availabilityDate = availabilityDto.getDate().atStartOfDay();
            if (availabilityDate.isBefore(startDate) || availabilityDate.isAfter(endDate))
                validationResults.put("availability", String.format("Availability date is invalid: %s", availabilityDto.getUuid()));
            if (availabilityDto.getFrom().isAfter(availabilityDto.getTo()))
                validationResults.put("availability", String.format("Availability hours is invalid: %s", availabilityDto.getUuid()));
        }
    }

    private static void trafficPrediction(RouteDto routeDto, Map<String, String> validationResults) {
        if (routeDto.getTrafficPrediction() == null)
            validationResults.put("trafficPrediction", "Traffic prediction needs to be set");
    }

    private static void startEndLocation(RouteDto routeDto, Map<String, String> validationResults) {
        if (routeDto.getStartLocation() == null)
            validationResults.put("startLocation", "Start location needs to be set");
        if (routeDto.getEndLocation() == null)
            validationResults.put("endLocation", "End location needs to be set");
    }

    private static void betweenLocations(RouteDto routeDto, Map<String, String> validationResults) {
        var routeLocations = routeDto.getLocations();
        if (routeLocations.size() < 2)
            validationResults.put("locations", "At least 2 route locations are required");
    }

}
