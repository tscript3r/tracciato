package pl.tscript3r.tracciato.duration.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

@Getter
@ToString
@AllArgsConstructor
public class DurationDto {

    private final Duration duration;
    private final long meters;

}
