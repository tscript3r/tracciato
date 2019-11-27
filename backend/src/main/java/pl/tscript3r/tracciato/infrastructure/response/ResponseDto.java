package pl.tscript3r.tracciato.infrastructure.response;

import lombok.Getter;

import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.*;

@Getter
public class ResponseDto {

    private final String status;
    private final Object payload;

    public static ResponseDto success(Object payload) {
        return of(SUCCESS, payload);
    }

    public static ResponseDto fail(Object payload) {
        return of(FAIL, payload);
    }

    public static ResponseDto error(Object payload) {
        return of(ERROR, payload);
    }

    public static ResponseDto of(ResponseStatus responseStatus, Object payload) {
        return new ResponseDto(responseStatus, payload);
    }

    private ResponseDto(ResponseStatus status, Object payload) {
        this.status = status.toString().toLowerCase();
        this.payload = payload;
    }

}