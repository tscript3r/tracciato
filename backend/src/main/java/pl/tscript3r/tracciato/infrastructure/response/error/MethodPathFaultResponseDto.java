package pl.tscript3r.tracciato.infrastructure.response.error;

import lombok.Getter;

@Getter
public class MethodPathFaultResponseDto extends FaultResponseDto {

    private final String method;
    private final String path;

    public MethodPathFaultResponseDto(String reason, String method, String path) {
        super(reason);
        this.method = method;
        this.path = path;
    }

}
