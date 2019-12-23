package pl.tscript3r.tracciato.route.location.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDto {

    private String name;
    private String country;
    private String zip;

    @NotNull
    @NotEmpty
    private String city;

    private String street;
    private String number;

}
