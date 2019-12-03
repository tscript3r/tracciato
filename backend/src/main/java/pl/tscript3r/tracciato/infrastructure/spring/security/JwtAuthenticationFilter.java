package pl.tscript3r.tracciato.infrastructure.spring.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Either;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.spring.util.ResponseEntityToHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.AUTH_MAPPING;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements AuthenticationFailureHandler {

    private final AuthenticationManager authenticationManager;
    private final ResponseResolver<ResponseEntity> responseResolver;

    JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                            ResponseResolver<ResponseEntity> responseResolver) {
        this.authenticationManager = authenticationManager;
        this.responseResolver = responseResolver;
        setFilterProcessesUrl(AUTH_MAPPING);
        setAuthenticationFailureHandler(this);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var username = request.getParameter("username");
        var password = request.getParameter("password");
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        var user = ((UsernamePasswordAuthenticationToken) authentication);

        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var signingKey = SecurityConstants.JWT_SECRET.getBytes();

        var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getName())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .claim("rol", roles)
                .compact();

        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        if (e instanceof CustomAuthenticationCredentialsNotFoundException) {
            var failureResponse = ((CustomAuthenticationCredentialsNotFoundException) e).getFailureResponse();
            ResponseEntityToHttpServletResponse.convert(responseResolver.resolve(Either.left(failureResponse)),
                    httpServletResponse);
        }
    }

}
