package pl.tscript3r.tracciato.infrastructure.response;

import lombok.Getter;
import lombok.ToString;

import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.*;

@Getter
@ToString
public class ResponseDto<T> {

    private final String status;
    private final T payload;

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
        return new ResponseDto<>(responseStatus, payload);
    }

    private ResponseDto(ResponseStatus status, T payload) {
        this.status = status.toString().toLowerCase();
        this.payload = payload;
    }

}