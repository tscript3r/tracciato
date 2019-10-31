package pl.tscript3r.tracciato.infrastructure.spring.security;

class SecurityConstants {

    // TODO externalize values

    static final String JWT_SECRET = "(G+KbPeShVmYq3t6w9z$B&E)H@McQfTjWnZr4u7x!A%D*F-JaNdRgUkXp2s5v8y/";
    static final String TOKEN_HEADER = "Authorization";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String TOKEN_TYPE = "JWT";
    static final String TOKEN_ISSUER = "tracciato";
    static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
    }

}
