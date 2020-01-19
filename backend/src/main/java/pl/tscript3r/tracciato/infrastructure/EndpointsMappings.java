package pl.tscript3r.tracciato.infrastructure;

public final class EndpointsMappings {

    private static final String BASE_V1_MAPPING = "/api/v1";
    public static final String ROUTE_UUID_VARIABLE = "routeUuid";
    public static final String USER_UUID_VARIABLE = "userUuid";
    public static final String LOCATION_UUID_VARIABLE = "locationUuid";

    public static final String AUTH_MAPPING = BASE_V1_MAPPING + "/authenticate";
    public static final String USER_MAPPING = BASE_V1_MAPPING + "/users";
    public static final String LOCATION_MAPPING = BASE_V1_MAPPING + "/locations";
    public static final String ROUTE_MAPPING = BASE_V1_MAPPING + "/routes";
    public static final String ROUTE_LOCATION_MAPPING = ROUTE_MAPPING + "/{" + ROUTE_UUID_VARIABLE + "}/locations";
    public static final String ROUTE_AVAILABILITY_MAPPING = ROUTE_MAPPING + "/{" + ROUTE_UUID_VARIABLE + "}/availability";

    public static final String ROUTE_START_LOCATION_MAPPING = "startLocation";
    public static final String ROUTE_END_LOCATION_MAPPING = "endLocation";

    public static final String[] PUBLIC_MAPPINGS = {"/*", "/h2-console/**", AUTH_MAPPING, USER_MAPPING};

    private EndpointsMappings() {
    }

}
