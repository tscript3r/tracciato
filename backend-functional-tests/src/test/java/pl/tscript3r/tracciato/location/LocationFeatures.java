package pl.tscript3r.tracciato.location;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.LOCATION_MAPPING;

@Slf4j
@Component
public class LocationFeatures extends AbstractFeatures {

    public JSONObject addLocation(String token, String locationJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, LOCATION_MAPPING, locationJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public JSONArray getLocations(String token, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(getRequest(token, LOCATION_MAPPING, null, expectedHttpStatus));
        try {
            return jsonObject.getJSONArray(PAYLOAD);
        } catch (JSONException e) {
            log.error(e.getLocalizedMessage(), e);
            return new JSONArray();
        }
    }

}
