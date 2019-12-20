package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.AbstractJson;
import pl.tscript3r.tracciato.route.api.NewRouteDto;

import java.time.LocalDateTime;

public class RouteJson extends AbstractJson<NewRouteDto> {

    private RouteJson(NewRouteDto newRouteDto) {
        super(newRouteDto);
    }

    public static RouteJson newValid() {
        return new RouteJson(RouteConst.getValidNewRouteDto());
    }

    public RouteJson name(String name) {
        object.setName(name);
        return this;
    }

    public RouteJson startDate(LocalDateTime localDateTime) {
        object.setStartDate(localDateTime);
        return this;
    }

    public RouteJson maxEndDate(LocalDateTime localDateTime) {
        object.setMaxEndDate(localDateTime);
        return this;
    }

    public LocalDateTime getStartDate() {
        return object.getStartDate();
    }

}
