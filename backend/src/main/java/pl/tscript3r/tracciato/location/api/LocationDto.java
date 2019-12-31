package pl.tscript3r.tracciato.location.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDto {

    private UUID uuid;

    @JsonIgnore
    private UUID ownerUuid;

    private String name;
    private String country;
    private String zip;

    @NotNull
    @NotEmpty
    private String city;

    private String street;
    private String number;

}
