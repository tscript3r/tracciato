package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.infrastructure.response.error.AbstractFailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

public class LocationFailureResponse extends AbstractFailureResponse {

    private static final String LOCATION_UUID_NOT_FOUND = "Location UUID not found";

    static FailureResponse uuidNotFound() {
        return new LocationFailureResponse(LOCATION_UUID_NOT_FOUND, 404);
    }

    private LocationFailureResponse(String reason, int httpStatus) {
        super(reason, httpStatus);
    }


}
