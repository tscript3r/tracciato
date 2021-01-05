package pl.tscript3r.tracciato.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.validator.BindingFailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.USER_MAPPING;

@Tag(name = "User", description = "Users API")
@RestController
@RequestMapping(USER_MAPPING)
@RequiredArgsConstructor
class UserSpringController {

    private final UserFacade userFacade;
    private final ResponseResolver<ResponseEntity<?>> responseResolver;

    @Operation(summary = "Registers new user", description = "Note that username and email needs to be unique. " +
            "Otherwise registration will fail due to validation process", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Successful registration",
                    content = {@Content(schema = @Schema(implementation = UserDto.class))}
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation fail",
                    content = {@Content(schema = @Schema(implementation = BindingFailureResponse.class)),})
    })
    @PostMapping
    public HttpEntity<?> registerUser(@RequestBody UserDto userDto) {
        return responseResolver.resolve(userFacade.register(userDto), HttpStatus.CREATED.value());
    }

    @GetMapping("secure")
    public String test() {
        return "ok";
    }

}

