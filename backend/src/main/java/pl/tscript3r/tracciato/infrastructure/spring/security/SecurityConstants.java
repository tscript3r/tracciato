package pl.tscript3r.tracciato.infrastructure.spring.security;

public class SecurityConstants {

    // TODO externalize values

    public static final String JWT_SECRET = "(G+KbPeShVmYq3t6w9z$B&E)H@McQfTjWnZr4u7x!A%D*F-JaNdRgUkXp2s5v8y/";
    static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "tracciato";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
    }

}
