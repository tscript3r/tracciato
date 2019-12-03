package pl.tscript3r.tracciato.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserSpringRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

}
