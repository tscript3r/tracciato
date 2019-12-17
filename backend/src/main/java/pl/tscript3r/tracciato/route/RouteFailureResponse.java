package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.infrastructure.response.error.AbstractFailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.UUID;

public class RouteFailureResponse extends AbstractFailureResponse {

    private static final String ROUTE_UUID_NOT_FOUND = "Route UUID not found";

    static FailureResponse uuidNotFound(UUID uuid) {
        return new RouteFailureResponse(ROUTE_UUID_NOT_FOUND, 404)
                .addField("uuid", uuid.toString());
    }

    private RouteFailureResponse(String reason, int httpStatus) {
        super(reason, httpStatus);
    }

}
