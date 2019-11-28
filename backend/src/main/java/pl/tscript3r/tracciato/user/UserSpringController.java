package pl.tscript3r.tracciato.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.user.api.UserDto;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.USER_MAPPING;

@RestController
@RequestMapping(USER_MAPPING)
@RequiredArgsConstructor
class UserSpringController {

    public static final String FIND_MAPPING = "find";
    public static final String USERNAME_PARAM = "username";
    private final UserFacade userFacade;
    private final ResponseResolver<ResponseEntity> responseResolver;

    @PostMapping
    public HttpEntity registerUser(@RequestBody UserDto userDto) {
        return responseResolver.resolve(userFacade.register(userDto), HttpStatus.CREATED.value());
    }

}
