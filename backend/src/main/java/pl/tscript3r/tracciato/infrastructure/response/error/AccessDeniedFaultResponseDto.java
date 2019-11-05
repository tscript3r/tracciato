package pl.tscript3r.tracciato.infrastructure.response.error;

import lombok.Getter;

@Getter
public class AccessDeniedFaultResponseDto extends FaultResponseDto {

    private final String method;
    private final String path;

    public AccessDeniedFaultResponseDto(String reason, String method, String path) {
        super(reason);
        this.method = method;
        this.path = path;
    }

}
