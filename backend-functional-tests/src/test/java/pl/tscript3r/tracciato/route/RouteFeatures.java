package pl.tscript3r.tracciato.route;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_MAPPING;

@Component
public class RouteFeatures extends AbstractFeatures {

    @Autowired
    UserFacade userFacade;

    @Autowired
    RouteFacade routeFacade;

    @Autowired
    RouteSpringRepository routeSpringRepository;

    public JSONObject addRoute(String token, String routeJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, ROUTE_MAPPING, routeJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public Boolean isRouteUuidExisting(UUID uuid) {
        return routeSpringRepository.findByUuid(uuid) != null;
    }

    @Override
    public void onApplicationEvent(@NotNull WebServerInitializedEvent webServerInitializedEvent) {
        this.servicePort = webServerInitializedEvent.getWebServer().getPort();
    }

}
