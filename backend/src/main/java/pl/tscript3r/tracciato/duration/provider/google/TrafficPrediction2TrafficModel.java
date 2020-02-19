package pl.tscript3r.tracciato.duration.provider.google;

import com.google.maps.model.TrafficModel;
import pl.tscript3r.tracciato.route.TrafficPrediction;

final class TrafficPrediction2TrafficModel {

    static TrafficModel map(TrafficPrediction trafficPrediction) {
        if (trafficPrediction == null)
            return null;
        switch (trafficPrediction) {
            case BEST_GUESS:
                return TrafficModel.BEST_GUESS;
            case OPTIMISTIC:
                return TrafficModel.OPTIMISTIC;
            case PESSIMISTIC:
                return TrafficModel.PESSIMISTIC;
            default:
                return null;
        }
    }

}
