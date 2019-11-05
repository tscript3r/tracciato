package pl.tscript3r.tracciato.infrastructure.response;

import lombok.Getter;

@Getter
class ResponseDto {

    private final String status;
    private final Object payload;

    ResponseDto(ResponseStatus status, Object payload) {
        this.status = status.toString().toLowerCase();
        this.payload = payload;
    }

}