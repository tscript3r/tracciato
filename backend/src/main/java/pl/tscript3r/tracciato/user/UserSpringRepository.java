package pl.tscript3r.tracciato.user;

import pl.tscript3r.tracciato.infrastructure.db.SpringRepository;

interface UserSpringRepository extends SpringRepository<UserEntity> {

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);

    UserEntity findByEmailIgnoreCase(String email);

    UserEntity findByUsernameIgnoreCase(String username);

}
