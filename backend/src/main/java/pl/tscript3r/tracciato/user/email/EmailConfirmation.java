package pl.tscript3r.tracciato.user.email;

import java.util.UUID;

public interface EmailConfirmation {

    Boolean confirm(String uuid);

    UUID create(Long id);

}
