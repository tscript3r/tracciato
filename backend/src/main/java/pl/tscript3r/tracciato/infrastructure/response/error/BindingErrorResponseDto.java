package pl.tscript3r.tracciato.infrastructure.response.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class BindingErrorResponseDto extends ErrorResponseDto {

    private Map<String, String> fieldsErrors;

    public BindingErrorResponseDto(String reason, Map<String, String> fieldsErrors) {
        super(reason);
        this.fieldsErrors = fieldsErrors;
    }

}
