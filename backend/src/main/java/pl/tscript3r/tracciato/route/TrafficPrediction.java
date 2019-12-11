package pl.tscript3r.tracciato.route;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TrafficPrediction {

    @JsonProperty("none")
    NONE,

    @JsonProperty("best_guess")
    BEST_GUESS,

    @JsonProperty("optimistic")
    OPTIMISTIC,

    @JsonProperty("pessimistic")
    PESSIMISTIC;

}
