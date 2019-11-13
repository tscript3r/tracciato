package pl.tscript3r.tracciato.infrastructure.spring.security;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.spring.util.ResponseEntityToHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseResolver<ResponseEntity> responseResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        ResponseEntityToHttpServletResponse.convert(
                responseResolver.getAccessDeniedResponse(request.getMethod(), request.getRequestURI()), response
        );
    }

}
