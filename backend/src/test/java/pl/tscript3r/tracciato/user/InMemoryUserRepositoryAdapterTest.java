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

    static InMemoryUserRepositoryAdapter getInMemoryUserRepositoryAdapter() {
        var result = new InMemoryUserRepositoryAdapter();
        result.save(UserEntityTest.getJohnUserEntity());
        return result;
    }

    @BeforeEach
    void setUp() {
        inMemoryUserRepositoryAdapter = getInMemoryUserRepositoryAdapter();
        savedJohnsEntity = inMemoryUserRepositoryAdapter.findByUsername(JOHNS_USERNAME)
                .get();
    }

    @Test
    void usernameExists_Should_ReturnTrue_When_SavedUsernameIsPassed() {
        // given
        var username = JOHNS_USERNAME;

        // when
        var result = inMemoryUserRepositoryAdapter.usernameExists(username);

        //then
        assertTrue(result);
    }

    @Test
    void usernameExists_Should_IgnoreCase_When_SameUsernameUpperCaseIsPassed() {
        // given
        var username = JOHNS_USERNAME.toUpperCase();

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
        var email = JOHNS_EMAIL;

        // when
        var result = inMemoryUserRepositoryAdapter.emailExists(email);

        //then
        assertTrue(result);
    }

    @Test
    void emailExists_Should_IgnoreCase_When_SameEmailUpperCaseIsPassed() {
        // given
        var email = JOHNS_EMAIL.toUpperCase();

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
        var email = JOHNS_EMAIL;

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
        var id = JOHNS_ID;

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
        eddysEntity.setId(JOHNS_ID);

        // when
        var result = inMemoryUserRepositoryAdapter.save(eddysEntity);

        // then
        assertTrue(inMemoryUserRepositoryAdapter.emailExists(result.getEmail()));
        assertFalse(inMemoryUserRepositoryAdapter.emailExists(savedJohnsEntity.getEmail()));
    }

    @Test
    void delete_Should_RemoveExistingEntity_When_ItsIdIsPassed() {
        // given
        var existingId = JOHNS_ID;

        // when
        inMemoryUserRepositoryAdapter.delete(existingId);

        // then
        assertTrue(inMemoryUserRepositoryAdapter.findById(existingId).isEmpty());
    }

    @Test
    void findByUsername_Should_SuccessfullyFindUser_When_GivenUsernameIsOwnedBySomeUser() {
        // given
        var existingUsername = JOHNS_USERNAME;

        // when
        var searchResults = inMemoryUserRepositoryAdapter.findByUsername(existingUsername);

        // then
        assertTrue(searchResults.isDefined());
        var foundUser = searchResults.get();
        assertEquals(savedJohnsEntity.getUsername(), foundUser.getUsername());
    }

    @Test
    void findByUsername_Should_NotFindAnyUser_When_GivenUsernameIsNotOwnedByAnyUser() {
        // given
        var nonExistingUsername = "Anonymous";

        // when
        var searchResults = inMemoryUserRepositoryAdapter.findByUsername(nonExistingUsername);

        // then
        assertTrue(searchResults.isEmpty());
    }

}