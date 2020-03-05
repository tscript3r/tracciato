package pl.tscript3r.tracciato.route.schedule.scheduler.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.RouteTimeline;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.RouteTimelineConst;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Route time")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RouteTimeTest {

    RouteTime routeTime;
    RouteDto routeDto;
    RouteTimeline routeTimeline;

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        routeTime = new RouteTime(routeDto);
        routeTimeline = routeTime.getRouteTimeline();
    }

    @Test
    void constructor_Should_SetFutureNowAsRouteStartDate_When_Created() {
        // then
        assertEquals(routeDto.getStartDate(), routeTime.getFutureNow());
    }

    @Test
    void constructor_Should_AddBeginningEventToTimeline_When_Created() {
        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.BEGINNING));
    }

    @Test
    void constructor_Should_AddOnlyOneEventToTimeline_When_Created() {
        // then
        assertEquals(1, routeTimeline.getEvents().size());
    }

    @Test
    void travel_Should_IncrementFutureNowWithTravelDuration_When_Called() {
        // given
        var duration = Duration.ofHours(1);
        var currentFutureNow = routeTime.getFutureNow();

        // when
        routeTime.travel(routeDto.getStartLocation(), routeDto.getEndLocation(), duration);

        // then
        assertEquals(currentFutureNow.plus(duration), routeTime.getFutureNow());
    }

    @Test
    void travel_Should_AddTravelStartEventToTimeline_When_Called() {
        // given
        var duration = Duration.ofHours(1);

        // when
        routeTime.travel(routeDto.getStartLocation(), routeDto.getEndLocation(), duration);

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.TRAVEL_START));
    }

    @Test
    void travel_Should_AddTravelArrivalEventToTimeline_When_Called() {
        // given
        var duration = Duration.ofHours(1);

        // when
        routeTime.travel(routeDto.getStartLocation(), routeDto.getEndLocation(), duration);

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.TRAVEL_ARRIVAL));
    }

    @Test
    void travel_Should_SetNextDayAndAddLeftTravelHoursToNewDay_When_TravelTimeTakesLongerThanWorkDayTime() {
        // given
        final int OVER_HOURS = 2;
        var date = routeDto.getStartDate();
        var currentDayDuration = getDayDuration(date.toLocalDate());
        var travelTimeDuration = currentDayDuration.plusHours(OVER_HOURS);

        // when
        routeTime.travel(routeDto.getStartLocation(), routeDto.getEndLocation(), travelTimeDuration);

        // then
        // date comparison - should be +1 day
        assertEquals(date.plusDays(1).toLocalDate(), routeTime.getFutureNow().toLocalDate());

        // time comparison - should be +2h from new day start time
        var nextDayStartTime = getAvailability(routeTime.getFutureNow().toLocalDate()).getFrom();
        assertEquals(nextDayStartTime.plusHours(OVER_HOURS), routeTime.getFutureNow().toLocalTime());
    }

    Duration getDayDuration(LocalDate date) {
        var currentDayAvailability = getAvailability(routeTime.getFutureNow().toLocalDate());
        return Duration.between(currentDayAvailability.getFrom(), currentDayAvailability.getTo());
    }

    AvailabilityDto getAvailability(LocalDate date) {
        return routeDto.getAvailabilities()
                .stream()
                .filter(a -> a.getDate().isEqual(date))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Could not find day availability for %s (most likely RouteConst.ValidRoute has been changed)", date))
                );
    }

    @Test
    void travel_Should_AddTravelTimeOverEventToTimeline_When_TravelTimeTakesLongerThanTravelHours() {
        // when
        forceNextDayThruTravel();

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.TRAVEL_TIME_OVER));
    }

    void forceNextDayThruTravel() {
        var currentDay = routeTime.getFutureNow().toLocalDate();
        var travelDuration = getDayDuration(currentDay).plusHours(2);
        routeTime.travel(routeDto.getStartLocation(), routeDto.getEndLocation(), travelDuration);
    }

    @Test
    void travel_Should_AddDayOverEventToTimeline_When_NextDayIsSet() {
        // when
        forceNextDayThruTravel();

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.DAY_OVER));
    }

    @Test
    void travel_Should_AddDayStartEventToTimeline_When_NextDayIsSet() {
        // when
        forceNextDayThruTravel();

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.DAY_START));
    }

    @Test
    void travel_Should_AddTravelContinueEventToTimeline_When_TravelTimeTakesLongerThanTravelHours() {
        // when
        forceNextDayThruTravel();

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.TRAVEL_CONTINUE));
    }

    @Test
    void travel_Should_AddRouteMaxEndingEventToTimeline_When_TotalRouteDurationTakesLongerThanMaxTimeSpecified() {
        // given
        routeDto.setMaxEndDate(RouteConst.START_DATE.plusDays(1));

        // when
        forceNextDayThruTravel();
        forceNextDayThruTravel(); // should add 3 days (second day is excluded) and max ending is after 1 day after start

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.ROUTE_MAX_ENDING_PASSED));
    }

    @Test
    void travel_Should_SkipNextDay_When_ItIsExcluded() {
        // given
        var startDate = routeDto.getStartDate();

        // when
        forceNextDayThruTravel();
        forceNextDayThruTravel(); // should add 3 days (second day is excluded) and max ending is after 1 day after start

        // then
        assertEquals(startDate.plusDays(3).toLocalDate(), routeTime.getFutureNow().toLocalDate());
    }

    @Test
    void travel_Should_AddDayExcludedToTimeline_When_AnExcludedDayWasSkipped() {
        // when
        forceNextDayThruTravel();
        forceNextDayThruTravel(); // should add 3 days (second day is excluded) and max ending is after 1 day after start

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.DAY_EXCLUDED));
    }

    @Test
    void onsite_Should_AddOnsiteDurationToFutureNow_When_Called() {
        // given
        var timeBefore = routeTime.getFutureNow();
        var routeLocation = routeDto.getLocations().iterator().next();
        routeLocation.setOnsideDuration("PT01H");

        // when
        routeTime.onsite(routeLocation);

        // then
        assertEquals(timeBefore.plusHours(1), routeTime.getFutureNow());
    }

    @Test
    void onsite_Should_SetNewDayAndAddDurationTime_When_OnsiteDurationTakesLongerThanTodaysLeftDuration() {
        // given
        var currentDate = routeTime.getFutureNow().toLocalDate();
        var currentDayDurationTime = getDayDuration(currentDate);
        var incrementDuration = currentDayDurationTime.minusHours(1);
        var routeLocation = routeDto.getLocations().iterator().next();
        routeLocation.setOnsideDuration("PT02H");
        routeTime.travel(routeDto.getStartLocation(), routeDto.getEndLocation(), incrementDuration); // 1h left for today

        // when
        routeTime.onsite(routeLocation);

        // then
        // date comparison - should be +1 day
        assertEquals(currentDate.plusDays(1), routeTime.getFutureNow().toLocalDate());
        // time comparison - should be +2h from start time
        var nextDayStartTime = getAvailability(routeTime.getFutureNow().toLocalDate()).getFrom();
        assertEquals(nextDayStartTime.plusHours(2), routeTime.getFutureNow().toLocalTime());
    }

    @Test
    void finish_Should_AddFinishEventToTimeline_When_Called() {
        // when
        routeTime.finish(routeDto.getEndLocation());

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.FINISH));
    }

    @Test
    void appointmentMismatched_Should_AddAppointmentMismatchedToTimeline_When_Called() {
        // when
        routeTime.appointmentMismatched(routeDto.getEndLocation());

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.ONSITE_APPOINTMENTS_MISMATCHED));
    }

    @Test
    void appointmentMatched_Should_AddAppointmentMatched_When_Called() {
        // when
        routeTime.appointmentMatched(routeDto.getEndLocation());

        // then
        assertTrue(RouteTimelineConst.isEventAdded(routeTimeline, RouteEvent.ONSITE_APPOINTMENTS_MATCHED));
    }

}