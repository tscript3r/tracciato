package pl.tscript3r.tracciato.infrastructure.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    CustomAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        var responseBody = ResponseResolver.getAccessDeniedResponse(request.getMethod(), request.getRequestURI());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(SC_FORBIDDEN);
        response.getWriter()
                .write(objectMapper.writeValueAsString(responseBody));
    }

}
