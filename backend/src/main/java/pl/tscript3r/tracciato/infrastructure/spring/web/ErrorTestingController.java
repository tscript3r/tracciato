package pl.tscript3r.tracciato.infrastructure.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Profile("errorController")
@RestController
@RequestMapping("/api/test")
public class ErrorTestingController {

    public ErrorTestingController() {
        log.warn("Error testing controller active");
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @GetMapping("not_implemented")
    public ResponseEntity<?> throwNotImplementedException() {
        throw new NotImplementedException("");
    }

    @GetMapping("any_exception")
    public ResponseEntity<?> throwAnyException() {
        throw new RuntimeException("");
    }

}
