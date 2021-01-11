package pl.tscript3r.tracciato.scheduled;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;
import pl.tscript3r.tracciato.schedule.ScheduleFeatures;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_SCHEDULED_MAPPING;

@Component
public class ScheduledFeatures extends AbstractFeatures {

    @Autowired
    UserFacade userFacade;

    @Autowired
    ScheduledSpringRepository scheduledSpringRepository;

    @Autowired
    ScheduleFeatures scheduleFeatures;

    @Autowired
    ScheduledFacade scheduledFacade;

    public String getScheduledMapping(UUID uuid) {
        return ROUTE_SCHEDULED_MAPPING + "/" + uuid.toString();
    }

    public JSONObject getScheduledJson(String token, UUID scheduledUuid, int expectedHttpStatus) throws JSONException {
        var json = new JSONObject(getRequest(token, getScheduledMapping(scheduledUuid), null, expectedHttpStatus));

        return json.getJSONObject(PAYLOAD);
    }

}
