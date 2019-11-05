package pl.tscript3r.tracciato.infrastructure.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FaultResponseDto {

    private final String reason;

}
