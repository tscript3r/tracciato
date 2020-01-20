package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.infrastructure.response.error.AbstractFailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

class RouteFailureResponse extends AbstractFailureResponse {

    private static final String ROUTE_UUID_NOT_FOUND = "Route UUID not found";

    static FailureResponse uuidNotFound() {
        return new RouteFailureResponse(ROUTE_UUID_NOT_FOUND, 404);
    }

    private RouteFailureResponse(String reason, int httpStatus) {
        super(reason, httpStatus);
    }

}
