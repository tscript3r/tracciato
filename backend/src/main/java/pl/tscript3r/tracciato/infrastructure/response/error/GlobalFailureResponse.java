package pl.tscript3r.tracciato.infrastructure.response.error;

public enum GlobalFailureResponse implements FailureResponse {

    INTERNAL_SERVER_ERROR(500, "Internal server error");

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
