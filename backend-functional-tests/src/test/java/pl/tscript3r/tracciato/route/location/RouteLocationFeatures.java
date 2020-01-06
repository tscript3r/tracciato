package pl.tscript3r.tracciato.route.location;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.*;

@Component
public class RouteLocationFeatures extends AbstractFeatures {

    public JSONObject addRouteLocation(String token, UUID routeUuid, String routeLocationJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, getRouteLocationMapping(routeUuid), routeLocationJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    private String getRouteLocationMapping(UUID routeUuid) {
        return ROUTE_LOCATION_MAPPING.replace("{" + ROUTE_UUID_VARIABLE + "}", routeUuid.toString());
    }

    public JSONObject setRouteStartLocation(String token, UUID routeUuid, String routeLocationJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, getRouteStartLocationMapping(routeUuid), routeLocationJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public JSONObject setRouteExistingStartLocation(String token, UUID routeUuid, UUID locationUuid, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(putRequest(token,
                getRouteStartLocationMapping(routeUuid) + "?" + LOCATION_UUID_VARIABLE + "=" + locationUuid.toString(),
                null,
                expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public JSONObject setRouteExistingEndLocation(String token, UUID routeUuid, UUID locationUuid, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(putRequest(token,
                getRouteEndLocationMapping(routeUuid) + "?" + LOCATION_UUID_VARIABLE + "=" + locationUuid.toString(),
                null,
                expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    private String getRouteStartLocationMapping(UUID routeUuid) {
        return getRouteLocationMapping(routeUuid) + "/" + ROUTE_START_LOCATION_MAPPING;
    }

    public JSONObject setRouteEndLocation(String token, UUID routeUuid, String routeLocationJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, getRouteEndLocationMapping(routeUuid), routeLocationJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    private String getRouteEndLocationMapping(UUID routeUuid) {
        return getRouteLocationMapping(routeUuid) + "/" + ROUTE_END_LOCATION_MAPPING;
    }

}
