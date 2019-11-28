package pl.tscript3r.tracciato.user;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
class UserEntity extends AbstractEntity {

    private UUID uuid;
    private String username;
    private String password;
    private String email;

}
