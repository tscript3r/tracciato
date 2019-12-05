package pl.tscript3r.tracciato.infrastructure.spring.security;

import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserFacade userFacade;

    JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserFacade userFacade) {
        super(authenticationManager);
        this.userFacade = userFacade;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        getAuthentication(request).peek(auth ->
                SecurityContextHolder.getContext().setAuthentication(auth)
        );
        filterChain.doFilter(request, response);
    }

    private Option<UsernamePasswordAuthenticationToken> getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        return userFacade.validateAndGetUuidFromToken(token)
                .map(uuid -> new UsernamePasswordAuthenticationToken(uuid, "", Collections.emptySet()));
    }

}