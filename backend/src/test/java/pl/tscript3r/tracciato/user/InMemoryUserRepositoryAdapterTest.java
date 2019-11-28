package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.user.UserEntityTest.*;

@DisplayName("In memory user repository adapter")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class InMemoryUserRepositoryAdapterTest {

    InMemoryUserRepositoryAdapter inMemoryUserRepositoryAdapter;
    UserEntity savedJohnsEntity;

    @BeforeEach
    void setUp() {
        inMemoryUserRepositoryAdapter = new InMemoryUserRepositoryAdapter();
        savedJohnsEntity = inMemoryUserRepositoryAdapter.save(UserEntityTest.getJohnUserEntity());
    }

    @Test
    void usernameExists_Should_ReturnTrue_When_SavedUsernameIsPassed() {
        // given
        var username = savedJohnsEntity.getUsername();

        // when
        var result = inMemoryUserRepositoryAdapter.usernameExists(username);

        //then
        assertTrue(result);
    }

    @Test
    void usernameExists_Should_IgnoreCase_When_SameUsernameUpperCaseIsPassed() {
        // given
        var username = savedJohnsEntity.getUsername().toUpperCase();

        // when
        var result = inMemoryUserRepositoryAdapter.usernameExists(username);

        //then
        assertTrue(result);
    }

    @Test
    void usernameExists_Should_ReturnFalse_When_UsernameIsNotExisting() {
        // given
        var username = EDY_USERNAME;

        // when
        var result = inMemoryUserRepositoryAdapter.usernameExists(username);

        //then
        assertFalse(result);
    }

    @Test
    void emailExists_Should_ReturnTrue_When_SavedEmailIsPassed() {
        // given
        var email = savedJohnsEntity.getEmail();

        // when
        var result = inMemoryUserRepositoryAdapter.emailExists(email);

        //then
        assertTrue(result);
    }

    @Test
    void emailExists_Should_IgnoreCase_When_SameEmailUpperCaseIsPassed() {
        // given
        var email = savedJohnsEntity.getEmail().toUpperCase();

        // when
        var result = inMemoryUserRepositoryAdapter.emailExists(email);

        //then
        assertTrue(result);
    }

    @Test
    void emailExists_Should_ReturnFalse_When_EmailIsNotExisting() {
        // given
        var email = EDY_EMAIL;

        // when
        var result = inMemoryUserRepositoryAdapter.emailExists(email);

        //then
        assertFalse(result);
    }

    @Test
    void findByEmail_Should_FindWantedEntity_When_ExistingEmailIsPassed() {
        // given
        var email = savedJohnsEntity.getEmail();

        // when
        var result = inMemoryUserRepositoryAdapter.findByEmail(email);

        // then
        assertTrue(result.contains(savedJohnsEntity));
    }

    @Test
    void findByEmail_Should_ReturnEmptyOption_When_NonExistingEmailIsPassed() {
        // given
        var email = EDY_EMAIL;

        // when
        var result = inMemoryUserRepositoryAdapter.findByEmail(email);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_Should_ReturnWantedEntity_When_ExistingIdIsPassed() {
        // given
        var id = savedJohnsEntity.getId();

        // when
        var result = inMemoryUserRepositoryAdapter.findById(id);

        // then
        assertTrue(result.contains(savedJohnsEntity));
    }

    @Test
    void save_Should_SaveEntityAndSetIdToIt_When_ValidEntityIsPassed() {
        // given
        var eddysEntity = getEdyUserEntity();
        eddysEntity.setId(null);

        // when
        var result = inMemoryUserRepositoryAdapter.save(eddysEntity);

        // then
        assertNotNull(result.getId());
        assertTrue(inMemoryUserRepositoryAdapter.emailExists(result.getEmail()));
    }

    @Test
    void save_Should_OverrideExistingEntity_When_EntityWithExistingIdIsPassed() {
        // given
        var eddysEntity = getEdyUserEntity();
        eddysEntity.setId(savedJohnsEntity.getId());

        // when
        var result = inMemoryUserRepositoryAdapter.save(eddysEntity);

        // then
        assertTrue(inMemoryUserRepositoryAdapter.emailExists(result.getEmail()));
        assertFalse(inMemoryUserRepositoryAdapter.emailExists(savedJohnsEntity.getEmail()));
    }

    @Test
    void delete_Should_RemoveExistingEntity_When_ItsIdIsPassed() {
        // given
        var existingId = savedJohnsEntity.getId();

        // when
        inMemoryUserRepositoryAdapter.delete(existingId);

        // then
        assertTrue(inMemoryUserRepositoryAdapter.findById(existingId).isEmpty());
    }

}