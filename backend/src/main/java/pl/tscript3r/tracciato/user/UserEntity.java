package pl.tscript3r.tracciato.user;

import lombok.Getter;
import lombok.Setter;
import pl.tscript3r.tracciato.infrastructure.db.AbstractEntity;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
class UserEntity extends AbstractEntity {

    private String username;
    private String password;
    private String email;

}
