package pl.tscript3r.tracciato.route.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LocationPriority {

    @JsonProperty("optional")
    OPTIONAL,

    @JsonProperty("medium")
    MEDIUM,

    @JsonProperty("high")
    HIGH;

}
