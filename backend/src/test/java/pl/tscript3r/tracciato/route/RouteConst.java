package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.route.api.NewRouteDto;

import java.time.LocalDateTime;
import java.util.Calendar;

public final class RouteConst {

    private static final Calendar CALENDAR = Calendar.getInstance();

    public static final String ROUTE_NAME = "CW52";
    public static final int START_DATE_YEAR = CALENDAR.get(Calendar.YEAR);
    public static final int START_DATE_MONTH = CALENDAR.get(Calendar.MONTH) + 1;
    public static final int START_DATE_DAY = CALENDAR.get(Calendar.DAY_OF_MONTH) + 1;
    public static final int START_DATE_HOUR = 8;
    public static final int START_DATE_MINUTE = 0;

    public static final LocalDateTime START_DATE =
            LocalDateTime.of(START_DATE_YEAR, START_DATE_MONTH, START_DATE_DAY, START_DATE_HOUR, START_DATE_MINUTE);

    public static final LocalDateTime MAX_END_DATE = START_DATE.plusDays(5).plusHours(8);

    public static final int MAX_END_DATE_YEAR = MAX_END_DATE.getYear();
    public static final int MAX_END_DATE_MONTH = MAX_END_DATE.getDayOfMonth();
    public static final int MAX_END_DATE_DAY = MAX_END_DATE.getDayOfMonth();
    public static final int MAX_END_DATE_HOUR = MAX_END_DATE.getHour();
    public static final int MAX_END_DATE_MINUTE = MAX_END_DATE.getMinute();

    public static final TrafficPrediction TRAFFIC_PREDICTION = TrafficPrediction.BEST_GUESS;

    public static NewRouteDto getValidNewRouteDto() {
        var newRouteDto = new NewRouteDto();
        newRouteDto.setStartDate(START_DATE);
        newRouteDto.setMaxEndDate(MAX_END_DATE);
        newRouteDto.setName(ROUTE_NAME);
        newRouteDto.setTraffic(TRAFFIC_PREDICTION);
        return newRouteDto;
    }

    private RouteConst() {
    }
}
