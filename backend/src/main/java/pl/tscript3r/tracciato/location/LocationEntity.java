package pl.tscript3r.tracciato.location;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
public class LocationEntity extends AbstractEntity {

    private UUID ownerUuid;
    private String name;
    private Long latitude;
    private Long longitude;
    private String country;
    private String zip;
    private String city;
    private String street;
    private String number;

}
