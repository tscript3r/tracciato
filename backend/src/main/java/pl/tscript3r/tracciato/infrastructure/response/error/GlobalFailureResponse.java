package pl.tscript3r.tracciato.infrastructure.response.error;

public enum GlobalFailureResponse implements FailureResponse {

    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    UNAUTHORIZED_FAILURE(401, "Unauthorized"),
    NOT_IMPLEMENTED_ERROR(501, "Not implemented yet"),
    NOT_FOUND(404, "Not found");

    private final int httpStatusCode;
    private final String reason;

    GlobalFailureResponse(int httpStatusCode, String reason) {
        this.httpStatusCode = httpStatusCode;
        this.reason = reason;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public Integer getHttpStatus() {
        return httpStatusCode;
    }

}
