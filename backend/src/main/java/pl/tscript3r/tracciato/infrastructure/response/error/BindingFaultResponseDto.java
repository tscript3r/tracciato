package pl.tscript3r.tracciato.infrastructure.response.error;

import lombok.Getter;

import java.util.Map;

@Getter
public class BindingFaultResponseDto extends FaultResponseDto {

    private final Map<String, String> fields;

    public BindingFaultResponseDto(String reason, Map<String, String> fields) {
        super(reason);
        this.fields = fields;
    }

}
