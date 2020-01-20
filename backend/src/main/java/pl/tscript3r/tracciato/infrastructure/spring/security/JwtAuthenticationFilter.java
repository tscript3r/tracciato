package pl.tscript3r.tracciato.infrastructure.spring.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.spring.util.ResponseEntityToHttpServletResponse;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.user.UserFailureResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.AUTH_MAPPING;

class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements AuthenticationFailureHandler {

    private final AuthenticationManager authenticationManager;
    private final ResponseResolver<ResponseEntity> responseResolver;
    private final UserFacade userFacade;

    JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                            ResponseResolver<ResponseEntity> responseResolver,
                            UserFacade userFacade) {
        this.authenticationManager = authenticationManager;
        this.responseResolver = responseResolver;
        this.userFacade = userFacade;
        setFilterProcessesUrl(AUTH_MAPPING);
        setAuthenticationFailureHandler(this);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var username = request.getParameter("username");
        var password = request.getParameter("password");
        if (username == null || password == null)
            throw new CustomAuthenticationCredentialsNotFoundException(UserFailureResponse.invalidCredentials());
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        var user = ((UsernamePasswordAuthenticationToken) authentication);
        userFacade.getToken(user.getName())
                .peek(s -> response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + s))
                .peekLeft(r -> ResponseEntityToHttpServletResponse.convert(responseResolver.resolve(r), response));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) {
        if (e instanceof CustomAuthenticationCredentialsNotFoundException) {
            var failureResponse = ((CustomAuthenticationCredentialsNotFoundException) e).getFailureResponse();
            ResponseEntityToHttpServletResponse.convert(responseResolver.resolve(failureResponse), response);
        }
    }

}
