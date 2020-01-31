package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.user.UserConst.*;

@DisplayName("In memory user repository adapter")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class UserInMemoryRepositoryAdapterTest {

    UserInMemoryRepositoryAdapter userInMemoryRepositoryAdapter;
    UserEntity savedJohnsEntity;

    static UserInMemoryRepositoryAdapter getInMemoryUserRepositoryAdapter() {
        var result = new UserInMemoryRepositoryAdapter();
        result.save(getJohnUserEntity());
        return result;
    }

    @BeforeEach
    void setUp() {
        userInMemoryRepositoryAdapter = getInMemoryUserRepositoryAdapter();
        savedJohnsEntity = userInMemoryRepositoryAdapter.findByUsername(JOHNS_USERNAME)
                .get();
    }

    @Test
    void usernameExists_Should_ReturnTrue_When_SavedUsernameIsPassed() {
        // given
        var username = JOHNS_USERNAME;

        // when
        var result = userInMemoryRepositoryAdapter.usernameExists(username);

        //then
        assertTrue(result);
    }

    @Test
    void usernameExists_Should_IgnoreCase_When_SameUsernameUpperCaseIsPassed() {
        // given
        var username = JOHNS_USERNAME.toUpperCase();

        // when
        var result = userInMemoryRepositoryAdapter.usernameExists(username);

        //then
        assertTrue(result);
    }

    @Test
    void usernameExists_Should_ReturnFalse_When_UsernameIsNotExisting() {
        // given
        var username = EDY_USERNAME;

        // when
        var result = userInMemoryRepositoryAdapter.usernameExists(username);

        //then
        assertFalse(result);
    }

    @Test
    void emailExists_Should_ReturnTrue_When_SavedEmailIsPassed() {
        // given
        var email = JOHNS_EMAIL;

        // when
        var result = userInMemoryRepositoryAdapter.emailExists(email);

        //then
        assertTrue(result);
    }

    @Test
    void emailExists_Should_IgnoreCase_When_SameEmailUpperCaseIsPassed() {
        // given
        var email = JOHNS_EMAIL.toUpperCase();

        // when
        var result = userInMemoryRepositoryAdapter.emailExists(email);

        //then
        assertTrue(result);
    }

    @Test
    void emailExists_Should_ReturnFalse_When_EmailIsNotExisting() {
        // given
        var email = EDY_EMAIL;

        // when
        var result = userInMemoryRepositoryAdapter.emailExists(email);

        //then
        assertFalse(result);
    }

    @Test
    void findByEmail_Should_FindWantedEntity_When_ExistingEmailIsPassed() {
        // given
        var email = JOHNS_EMAIL;

        // when
        var result = userInMemoryRepositoryAdapter.findByEmail(email);

        // then
        assertTrue(result.contains(savedJohnsEntity));
    }

    @Test
    void findByEmail_Should_ReturnEmptyOption_When_NonExistingEmailIsPassed() {
        // given
        var email = EDY_EMAIL;

        // when
        var result = userInMemoryRepositoryAdapter.findByEmail(email);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_Should_ReturnWantedEntity_When_ExistingIdIsPassed() {
        // given
        var id = JOHNS_ID;

        // when
        var result = userInMemoryRepositoryAdapter.findById(id);

        // then
        assertTrue(result.contains(savedJohnsEntity));
    }

    @Test
    void save_Should_SaveEntityAndSetIdToIt_When_ValidEntityIsPassed() {
        // given
        var eddysEntity = getEdyUserEntity();
        eddysEntity.setId(null);

        // when
        var result = userInMemoryRepositoryAdapter.save(eddysEntity);

        // then
        assertNotNull(result.getId());
        assertTrue(userInMemoryRepositoryAdapter.emailExists(result.getEmail()));
    }

    @Test
    void save_Should_OverrideExistingEntity_When_EntityWithExistingIdIsPassed() {
        // given
        var eddysEntity = getEdyUserEntity();
        eddysEntity.setId(JOHNS_ID);

        // when
        var result = userInMemoryRepositoryAdapter.save(eddysEntity);

        // then
        assertTrue(userInMemoryRepositoryAdapter.emailExists(result.getEmail()));
        assertFalse(userInMemoryRepositoryAdapter.emailExists(savedJohnsEntity.getEmail()));
    }

    @Test
    void delete_Should_RemoveExistingEntity_When_ItsIdIsPassed() {
        // given
        var existingId = JOHNS_ID;

        // when
        userInMemoryRepositoryAdapter.delete(existingId);

        // then
        assertTrue(userInMemoryRepositoryAdapter.findById(existingId).isEmpty());
    }

    @Test
    void findByUsername_Should_SuccessfullyFindUser_When_GivenUsernameIsExisting() {
        // given
        var existingUsername = JOHNS_USERNAME;

        // when
        var searchResults = userInMemoryRepositoryAdapter.findByUsername(existingUsername);

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
        var searchResults = userInMemoryRepositoryAdapter.findByUsername(nonExistingUsername);

        // then
        assertTrue(searchResults.isEmpty());
    }

    @Test
    void findByUuid_Should_FindByUuid_When_GivenUuidIsExisting() {
        // given
        var existingUuid = JOHNS_UUID;

        // when
        var searchResults = userInMemoryRepositoryAdapter.findByUuid(existingUuid);

        // then
        assertTrue(searchResults.isDefined());
    }

    @Test
    void findByUuid_Should_NotFindByUuid_When_GivenUuidIsNotExisting() {
        // given
        var existingUuid = UUID.randomUUID();

        // when
        var searchResults = userInMemoryRepositoryAdapter.findByUuid(existingUuid);

        // then
        assertTrue(searchResults.isEmpty());
    }

}