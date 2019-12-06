package pl.tscript3r.tracciato.route.location;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class LocationEntity extends AbstractEntity {

    private String name;
    private Long latitude;
    private Long longitude;
    private String country;
    private String zip;
    private String city;
    private String street;
    private String number;

}
