package pl.tscript3r.tracciato.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ResponseStatus {

    @JsonProperty("success")
    SUCCESS(100, 299),
    @JsonProperty("fail")
    FAIL(400, 499),
    @JsonProperty("error")
    ERROR(500, 599);

    private final int httpStatusFromRange;
    private final int httpStatusToRange;

    ResponseStatus(int httpStatusFromRange, int httpStatusToRange) {
        this.httpStatusFromRange = httpStatusFromRange;
        this.httpStatusToRange = httpStatusToRange;
    }

    public static ResponseStatus get(int httpStatus) {
        for (ResponseStatus responseStatus : values())
            if (responseStatus.httpStatusFromRange <= httpStatus && httpStatus <= responseStatus.httpStatusToRange)
                return responseStatus;

        throw new IllegalArgumentException(String.format("Given http status: %d is invalid", httpStatus));
    }

}
