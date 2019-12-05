package pl.tscript3r.tracciato.infrastructure.spring.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.Collections;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserFacade userFacade;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        return map(userFacade.login(username, password)
                .getOrElseThrow(CustomAuthenticationCredentialsNotFoundException::new)
        );
    }

    private Authentication map(UserDto userDto) {
        return new UsernamePasswordAuthenticationToken(
                userDto.getUsername(), userDto.getPassword(), Collections.emptySet());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
