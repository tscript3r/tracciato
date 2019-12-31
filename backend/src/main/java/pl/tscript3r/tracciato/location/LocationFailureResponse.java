package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.infrastructure.response.error.AbstractFailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.UUID;

public class LocationFailureResponse extends AbstractFailureResponse {

    private static final String LOCATION_UUID_NOT_FOUND = "Location UUID not found";

    static FailureResponse uuidNotFound(UUID uuid) {
        return new LocationFailureResponse(LOCATION_UUID_NOT_FOUND, 404)
                .addField("uuid", uuid.toString());
    }

    private LocationFailureResponse(String reason, int httpStatus) {
        super(reason, httpStatus);
    }


}
