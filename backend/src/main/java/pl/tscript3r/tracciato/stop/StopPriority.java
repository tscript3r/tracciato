package pl.tscript3r.tracciato.stop;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StopPriority {

    @JsonProperty("optional")
    OPTIONAL,

    @JsonProperty("medium")
    MEDIUM,

    @JsonProperty("high")
    HIGH;

}
