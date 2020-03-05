package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.*;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Route timeline")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RouteTimelineTest {

    RouteTimeline routeTimeline;
    LocalDateTime date;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.now();
        routeTimeline = new RouteTimeline();
    }

    @Test
    void beginning_Should_AddBeginningEventToTimeline_When_Called() {
        // when
        routeTimeline.beginning(date);

        // then
        var event = getEvent(RouteEvent.BEGINNING);
        assertTimelineEvent(RouteEvent.BEGINNING, event);
    }

    TimelineEvent getEvent(RouteEvent routeEvent) {
        return routeTimeline.getEvents()
                .stream()
                .filter(timelineEvent -> timelineEvent.getRouteEvent().equals(routeEvent))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    void assertTimelineEvent(RouteEvent routeEvent, TimelineEvent timelineEvent) {
        assertEquals(routeEvent, timelineEvent.getRouteEvent());
        assertEquals(date, timelineEvent.getBeginning());
    }

    @Test
    void dayStart_Should_AddDayStartEventToTimeline_When_Called() {
        // when
        routeTimeline.dayStart(date);

        // then
        var event = getEvent(RouteEvent.DAY_START);
        assertTimelineEvent(RouteEvent.DAY_START, event);
    }

    @Test
    void dayOver_Should_AddDayOverEventToTimeline_When_Called() {
        // when
        routeTimeline.dayOver(date);

        // then
        var event = getEvent(RouteEvent.DAY_OVER);
        assertTimelineEvent(RouteEvent.DAY_OVER, event);
    }

    @Test
    void travelArrival_Should_AddTravelArrivalEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getBrunszwikLocationDto();

        // when
        routeTimeline.travelArrival(date, location);

        // then
        var event = getEvent(RouteEvent.TRAVEL_ARRIVAL);
        assertLocationTimelineEvent(RouteEvent.TRAVEL_ARRIVAL, location, event);
    }

    void assertLocationTimelineEvent(RouteEvent routeEvent, LocationDto location, TimelineEvent locationTimelineEvent) {
        assertTimelineEvent(routeEvent, locationTimelineEvent);
        assertTrue(locationTimelineEvent instanceof LocationTimelineEvent);
        assertEquals(location, ((LocationTimelineEvent) locationTimelineEvent).getLocationDto());
    }

    @Test
    void finish_Should_AddFinishEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getBrunszwikLocationDto();

        // when
        routeTimeline.finish(date, location);

        // then
        var event = getEvent(RouteEvent.FINISH);
        assertLocationTimelineEvent(RouteEvent.FINISH, location, event);
    }

    @Test
    void onsite_Should_AddOnsiteEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getBrunszwikLocationDto();
        var duration = Duration.ZERO;

        // when
        routeTimeline.onsite(date, duration, location);

        // then
        var event = getEvent(RouteEvent.ONSITE);
        assertDurationLocationTimelineEvent(RouteEvent.ONSITE, location, duration, event);
    }

    void assertDurationLocationTimelineEvent(RouteEvent routeEvent, LocationDto location, Duration duration,
                                             TimelineEvent durationLocationTimelineEvent) {
        assertLocationTimelineEvent(routeEvent, location, durationLocationTimelineEvent);
        assertEquals(duration, ((DurationLocationTimelineEvent) durationLocationTimelineEvent).getDuration());
    }

    @Test
    void travelContinue_Should_AddTravelContinueEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getBrunszwikLocationDto();
        var duration = Duration.ZERO;

        // when
        routeTimeline.travelContinue(date, duration, location);

        // then
        var event = getEvent(RouteEvent.TRAVEL_CONTINUE);
        assertDurationLocationTimelineEvent(RouteEvent.TRAVEL_CONTINUE, location, duration, event);
    }

    @Test
    void travelStart_Should_AddTravelStartEventToTimeline_When_Called() {
        // given
        var fromLocation = LocationConst.getBremaLocationDto();
        var destination = LocationConst.getBerlinLocationDto();
        var duration = Duration.ZERO;

        // when
        routeTimeline.travelStart(date, fromLocation, destination, duration);

        // then
        var event = getEvent(RouteEvent.TRAVEL_START);
        assertTravelLocationTimelineEvent(RouteEvent.TRAVEL_START, fromLocation, destination, duration, event);
    }

    private void assertTravelLocationTimelineEvent(RouteEvent routeEvent, LocationDto fromLocation,
                                                   LocationDto destination, Duration duration, TimelineEvent event) {
        assertDurationLocationTimelineEvent(routeEvent, fromLocation, duration, event);
        assertEquals(destination, ((TravelLocationTimelineEvent) event).getDestination());
    }

    @Test
    void routeMaxEndingPassed_Should_AddRouteMaxEndingPassedEventToTimeline_When_Called() {
        // when
        routeTimeline.routeMaxEndingPassed(date);

        // then
        var event = getEvent(RouteEvent.ROUTE_MAX_ENDING_PASSED);
        assertTimelineEvent(RouteEvent.ROUTE_MAX_ENDING_PASSED, event);
    }

    @Test
    void travelTimeOver_Should_AddTravelTimeOverEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getLuneburgLocationDto();
        var duration = Duration.ZERO;

        // when
        routeTimeline.travelTimeOver(date, location, duration);

        // then
        var event = getEvent(RouteEvent.TRAVEL_TIME_OVER);
        assertDurationLocationTimelineEvent(RouteEvent.TRAVEL_TIME_OVER, location, duration, event);
    }

    @Test
    void dayExcluded_Should_AddDayExcludedEventToTimeline_When_Called() {
        // when
        routeTimeline.dayExcluded(date.toLocalDate());

        // then
        var event = getEvent(RouteEvent.DAY_EXCLUDED);
        assertEquals(RouteEvent.DAY_EXCLUDED, event.getRouteEvent());
        assertEquals(date.toLocalDate().atStartOfDay(), event.getBeginning());
    }

    @Test
    void appointmentMismatched_Should_AddAppointmentMismatchedEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getBerlinLocationDto();

        // when
        routeTimeline.appointmentMismatched(date, location);

        // then
        var event = getEvent(RouteEvent.ONSITE_APPOINTMENTS_MISMATCHED);
        assertLocationTimelineEvent(RouteEvent.ONSITE_APPOINTMENTS_MISMATCHED, location, event);
    }

    @Test
    void appointmentMatched_Should_AddAppointmentMatchedEventToTimeline_When_Called() {
        // given
        var location = LocationConst.getBerlinLocationDto();

        // when
        routeTimeline.appointmentMatched(date, location);

        // then
        var event = getEvent(RouteEvent.ONSITE_APPOINTMENTS_MATCHED);
        assertLocationTimelineEvent(RouteEvent.ONSITE_APPOINTMENTS_MATCHED, location, event);
    }

}