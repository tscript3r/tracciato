package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.AbstractInMemoryRepositoryAdapter;

import java.util.UUID;

public class UserInMemoryRepositoryAdapter extends AbstractInMemoryRepositoryAdapter<UserEntity> implements
        UserRepositoryAdapter {

    @Override
    public Boolean usernameExists(String username) {
        return db.values().stream()
                .anyMatch(userEntity ->
                        userEntity.getUsername()
                                .equalsIgnoreCase(username)
                );
    }

    @Override
    public Boolean emailExists(String email) {
        return db.values().stream()
                .anyMatch(userEntity ->
                        userEntity.getEmail()
                                .equalsIgnoreCase(email)
                );
    }

    @Override
    public Option<UserEntity> findByEmail(String email) {
        return find(userEntity -> userEntity.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public Option<UserEntity> findByUsername(String username) {
        return find(userEntity -> userEntity.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public Option<UserEntity> findByUuid(UUID uuid) {
        return find(userEntity -> userEntity.getUuid().equals(uuid));
    }

}
