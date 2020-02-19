package pl.tscript3r.tracciato.duration.provider;

import java.util.concurrent.Future;

public interface DurationProvider {

    Future<DurationDto> getQuickestTravelDuration(String from, String to);

}
