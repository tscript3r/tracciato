package pl.tscript3r.tracciato.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface UserSpringRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);

    UserEntity findByEmailIgnoreCase(String email);

    UserEntity findByUsernameIgnoreCase(String username);

    UserEntity findByUuid(UUID uuid);

}
