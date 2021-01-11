package pl.tscript3r.tracciato.schedule;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;
import pl.tscript3r.tracciato.utils.ConcurrentStressTest;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.*;

@Component
public class ScheduleFeatures extends AbstractFeatures implements ConcurrentStressTest {

    public String getScheduleMapping(UUID routeUuid, Boolean wait) {
        var result = ROUTE_SCHEDULE_MAPPING.replace("{" + ROUTE_UUID_VARIABLE + "}", routeUuid.toString());
        return wait != null ? result + "?wait=" + wait.toString() : result;
    }

    public String getValidateMapping(UUID routeUuid) {
        return getScheduleMapping(routeUuid, null) + "/" + ROUTE_SCHEDULE_VALIDATION_MAPPING;
    }

    public JSONObject validate(String token, UUID routeUuid, int expectedHttpStatus) throws JSONException {
        var json = new JSONObject(
                postRequest(token, getValidateMapping(routeUuid), null, expectedHttpStatus)
        );
        return json.getJSONObject(PAYLOAD);
    }

    public JSONObject schedule(String token, UUID routeUuid, Boolean wait, int expectedHttpStatus) throws JSONException {
        var json = new JSONObject(
                postRequest(token, getScheduleMapping(routeUuid, wait), null, expectedHttpStatus)
        );
        return json.getJSONObject(PAYLOAD);
    }

}
