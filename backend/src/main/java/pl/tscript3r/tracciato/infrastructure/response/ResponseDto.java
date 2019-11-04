package pl.tscript3r.tracciato.infrastructure.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class ResponseDto {

    private String status;
    private Object payload;

    ResponseDto(ResponseStatus status, Object payload) {
        this.status = status.toString().toLowerCase();
        this.payload = payload;
    }

}