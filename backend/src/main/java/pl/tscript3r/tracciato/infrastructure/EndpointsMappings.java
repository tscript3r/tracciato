package pl.tscript3r.tracciato.infrastructure;

public class EndpointsMappings {

    private static final String BASE_V1_MAPPING = "/api/v1";

    public static final String AUTH_MAPPING = BASE_V1_MAPPING + "/authenticate";
    public static final String USER_MAPPING = BASE_V1_MAPPING + "/users";
    public static final String ROUTE_MAPPING = BASE_V1_MAPPING + "/route";

    public static final String[] PUBLIC_MAPPINGS = {"/*", "/h2-console/**", AUTH_MAPPING, USER_MAPPING};

    private EndpointsMappings() {
    }

}
