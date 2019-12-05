package pl.tscript3r.tracciato.user;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.*;

@Slf4j
@AllArgsConstructor
class JWTTokenResolver {

    private final byte[] signingKey = JWT_SECRET.getBytes();

    String getToken(UUID uuid) throws InvalidKeyException {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(uuid.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
    }

    Option<UUID> getUuidAndValidateToken(String token) {
        if (StringUtils.isNotEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            try {
                return parseToken(token)
                        .map(this::extractUuidFromJws)
                        .map(UUID::fromString);
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            }
        }
        return Option.none();
    }

    private Option<Jws<Claims>> parseToken(String token) {
        try {
            return Option.of(Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")));
        } catch (ExpiredJwtException exception) {
            log.debug("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.debug("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        return Option.none();
    }

    private String extractUuidFromJws(Jws<Claims> jws) {
        return jws.getBody().getSubject();
    }

}
