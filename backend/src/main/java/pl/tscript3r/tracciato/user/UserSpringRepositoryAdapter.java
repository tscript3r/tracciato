package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.SpringRepositoryAdapter;

class UserSpringRepositoryAdapter extends SpringRepositoryAdapter<UserEntity> implements UserRepositoryAdapter {

    private final UserSpringRepository userSpringRepository;

    public UserSpringRepositoryAdapter(UserSpringRepository userSpringRepository) {
        super(userSpringRepository);
        this.userSpringRepository = userSpringRepository;
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

}
