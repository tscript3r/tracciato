package pl.tscript3r.tracciato.user;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.*;

@Slf4j
class JWTTokenResolver {

    private final byte[] signingKey = JWT_SECRET.getBytes();
    private final LoadingCache<String, Option<UUID>> tokenCache;

    public JWTTokenResolver() {
        this.tokenCache = buildCache();
    }

    private LoadingCache<String, Option<UUID>> buildCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(60, TimeUnit.MINUTES)
                .build(cacheLoader());
    }

    private CacheLoader<String, Option<UUID>> cacheLoader() {
        return new CacheLoader<>() {
            @Override
            public Option<UUID> load(String key) {
                return getUuid(key);
            }
        };
    }

    String getToken(UUID uuid) throws InvalidKeyException {
        var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(uuid.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
        tokenCache.put(token, Option.of(uuid));
        return token;
    }

    Option<UUID> getUuidAndValidateToken(String token) {
        if (StringUtils.isNotEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            try {
                return tokenCache.get(removeBearer(token));
            } catch (ExecutionException e) {
                log.warn(e.getMessage(), e);
                return Option.none();
            }
        }
        return Option.none();
    }

    private String removeBearer(String token) {
        return token.replace(TOKEN_PREFIX, "");
    }

    private Option<UUID> getUuid(String token) {
        try {
            return parseToken(token)
                    .map(this::extractUuidFromJws)
                    .map(UUID::fromString);
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        }
        return Option.none();
    }

    private String extractUuidFromJws(Jws<Claims> jws) {
        return jws.getBody().getSubject();
    }

    private Option<Jws<Claims>> parseToken(String token) {
        try {
            return Option.of(Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(removeBearer(token)));
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

}
