package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

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
        return Option.ofOptional(userSpringRepository.findByEmailIgnoreCase(email));
    }

    @Override
    public Option<UserEntity> findByUsername(String username) {
        return Option.ofOptional(userSpringRepository.findByUsernameIgnoreCase(username));
    }

}
