package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

public enum RouteEvent {

    // Route beginning
    BEGINNING,

    // Indicates travel start to next location
    TRAVEL_START,

    // Indicates that travel to next location takes longer than work time of the current day. Assuming that the
    // traveller ends his work day and waits for a new day to travel the rest of the road
    TRAVEL_TIME_OVER,

    // Indicates that traveller has waited for a new day to finish the road to next location
    TRAVEL_CONTINUE,

    // Traveller arrived to next location
    TRAVEL_ARRIVAL,

    // Traveller reached the location and will spend the assumed time onsite
    ONSITE,

    ONSITE_APPOINTMENTS_MATCHED,

    ONSITE_APPOINTMENTS_MISMATCHED,

    // Indicates that a day was excluded - traveller waits until next day on his current location
    DAY_EXCLUDED,

    // New day
    DAY_START,

    // Day work hours are over
    DAY_OVER,

    // Traveller reached the end location and the route is over
    FINISH,

    // Point where route duration is already longer than the user declared the max duration
    ROUTE_MAX_ENDING_PASSED;

}
