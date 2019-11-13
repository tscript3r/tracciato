package pl.tscript3r.tracciato.infrastructure.spring.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class ResponseEntityToHttpServletResponse {

    private ResponseEntityToHttpServletResponse() {
    }

    // https://stackoverflow.com/questions/40162392/how-to-write-responseentity-to-httpservletresponse/43845840
    public static void convert(ResponseEntity responseEntity, HttpServletResponse httpServletResponse) throws IOException {
        for (Map.Entry<String, List<String>> header : responseEntity.getHeaders().entrySet()) {
            String key = header.getKey();
            for (String value : header.getValue())
                httpServletResponse.addHeader(key, value);
        }
        httpServletResponse.setStatus(responseEntity.getStatusCodeValue());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        if (responseEntity.getBody() != null)
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }

}
