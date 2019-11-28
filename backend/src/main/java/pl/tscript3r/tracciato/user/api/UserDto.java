package pl.tscript3r.tracciato.user.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    @NotEmpty
    @Size(min = 4, max = 12)
    private String username;

    @NotEmpty
    @Size(min = 6, max = 32)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty
    @Email
    @Size(max = 64)
    private String email;

}
