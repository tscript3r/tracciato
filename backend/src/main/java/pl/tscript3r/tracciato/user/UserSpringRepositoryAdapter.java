package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
class UserSpringRepositoryAdapter implements UserRepositoryAdapter {

    private final UserSpringRepository userSpringRepository;

    @Override
    public Option<UserEntity> findById(Long id) {
        return Option.ofOptional(userSpringRepository.findById(id));
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return userSpringRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        userSpringRepository.deleteById(id);
    }

    @Override
    public Boolean usernameExists(String username) {
        return userSpringRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public Boolean emailExists(String email) {
        return userSpringRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Option<UserEntity> findByEmail(String email) {
        return Option.of(userSpringRepository.findByEmailIgnoreCase(email));
    }

    @Override
    public Option<UserEntity> findByUsername(String username) {
        return Option.of(userSpringRepository.findByUsernameIgnoreCase(username));
    }

    @Override
    public Option<UserEntity> findByUuid(UUID uuid) {
        return Option.of(userSpringRepository.findByUuid(uuid));
    }
}
