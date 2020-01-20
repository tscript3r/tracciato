package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;

interface UserRepositoryAdapter extends RepositoryAdapter<Long, UserEntity> {

    Boolean usernameExists(String username);

    Boolean emailExists(String email);

    Option<UserEntity> findByEmail(String email);

    Option<UserEntity> findByUsername(String username);

}
