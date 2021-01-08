package pl.tscript3r.tracciato.stop;

import pl.tscript3r.tracciato.infrastructure.response.error.AbstractFailureResponse;

class StopFailureResponse extends AbstractFailureResponse {

    private StopFailureResponse(String reason, int httpStatus) {
        super(reason, httpStatus);
    }

    static StopFailureResponse existingOrNewLocationRequired() {
        return new StopFailureResponse("Existing or new location required", 400);
    }

}
