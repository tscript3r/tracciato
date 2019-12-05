package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class InMemoryUserRepositoryAdapter implements UserRepositoryAdapter {

    private final Map<Long, UserEntity> dbMap = new HashMap<>();

    @Override
    public Boolean usernameExists(String username) {
        return dbMap.values().stream()
                .anyMatch(userEntity ->
                        userEntity.getUsername()
                                .equalsIgnoreCase(username)
                );
    }

    @Override
    public Boolean emailExists(String email) {
        return dbMap.values().stream()
                .anyMatch(userEntity ->
                        userEntity.getEmail()
                                .equalsIgnoreCase(email)
                );
    }

    @Override
    public Option<UserEntity> findByEmail(String email) {
        return find(userEntity -> userEntity.getEmail().equalsIgnoreCase(email));
    }

    private Option<UserEntity> find(Predicate<UserEntity> userEntityPredicate) {
        return Option.ofOptional(dbMap.values().stream()
                .filter(userEntityPredicate)
                .findFirst());
    }

    @Override
    public Option<UserEntity> findById(Long id) {
        return Option.of(dbMap.get(id));
    }

    @Override
    public UserEntity save(UserEntity entity) {
        long id = (entity.getId() == null) ? dbMap.size() + 1 : entity.getId();
        entity.setId(id);
        dbMap.put(id, entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        dbMap.remove(id);
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
