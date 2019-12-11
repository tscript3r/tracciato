package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.route.api.NewRouteDto;

import java.time.LocalDateTime;

public class RouteConst {

    public static final String ROUTE_NAME = "CW52";
    public static final int START_DATE_YEAR = 2019;
    public static final int START_DATE_MONTH = 12;
    public static final int START_DATE_DAY = 23;
    public static final int START_DATE_HOUR = 8;
    public static final int START_DATE_MINUTE = 30;

    public static final int MAX_END_DATE_YEAR = 2019;
    public static final int MAX_END_DATE_MONTH = 12;
    public static final int MAX_END_DATE_DAY = 27;
    public static final int MAX_END_DATE_HOUR = 18;
    public static final int MAX_END_DATE_MINUTE = 0;

    public static final LocalDateTime START_DATE =
            LocalDateTime.of(START_DATE_YEAR, START_DATE_MONTH, START_DATE_DAY, START_DATE_HOUR, START_DATE_MINUTE);

    public static final LocalDateTime MAX_END_DATE =
            LocalDateTime.of(MAX_END_DATE_YEAR, MAX_END_DATE_MONTH, MAX_END_DATE_DAY, MAX_END_DATE_HOUR, MAX_END_DATE_MINUTE);

    public static final TrafficPrediction TRAFFIC_PREDICTION = TrafficPrediction.BEST_GUESS;

    static NewRouteDto getValidNewRouteDto() {
        var newRouteDto = new NewRouteDto();
        newRouteDto.setStartDate(START_DATE);
        newRouteDto.setMaxEndDate(MAX_END_DATE);
        newRouteDto.setName(ROUTE_NAME);
        newRouteDto.setTraffic(TRAFFIC_PREDICTION);
        return newRouteDto;
    }

}
