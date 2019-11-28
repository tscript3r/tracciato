package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User entity")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class UserEntityTest {

    public static final String JOHNS_USERNAME = "John";
    public static final String JOHNS_EMAIL = "johns@email.com";
    public static final String JOHNS_PASSWORD = "johnsPassword";
    public static final String EDY_USERNAME = "Edy";
    public static final String EDY_EMAIL = "edys@mail.com";
    public static final String EDY_PASSWORD = "edysPassword";

    public static UserEntity getJohnUserEntity() {
        var user = new UserEntity();
        user.setId(1L);
        user.setUsername(JOHNS_USERNAME);
        user.setEmail(JOHNS_EMAIL);
        user.setPassword(JOHNS_PASSWORD);
        return user;
    }

    public static UserEntity getEdyUserEntity() {
        var user = new UserEntity();
        user.setId(2L);
        user.setUsername(EDY_USERNAME);
        user.setEmail(EDY_EMAIL);
        user.setPassword(EDY_PASSWORD);
        return user;
    }

    @Test
    void isNew_Should_ReturnTrue_When_IdIsNotSet() {
        var user = new UserEntity();
        assertTrue(user.isNew());
    }

    @Test
    void isNew_Should_ReturnFalse_When_IdIsSet() {
        var user = new UserEntity();
        user.setId(1L);
        assertFalse(user.isNew());
    }

    @Test
    void equals_Should_NotBeEqual_When_UserEntitiesHaveDifferentIds() {
        // given
        var johnUserEntity = getJohnUserEntity();
        var edyUserEntity = getEdyUserEntity();

        // when
        johnUserEntity.setId(1L);
        edyUserEntity.setId(2L);

        // then
        assertNotEquals(johnUserEntity, edyUserEntity);
    }

    @Test
    void equals_Should_BeEqual_When_UserEntitiesHaveSameIds() {
        // given
        var johnUserEntity = getJohnUserEntity();
        var edyUserEntity = getEdyUserEntity();

        // when
        johnUserEntity.setId(1L);
        edyUserEntity.setId(1L);

        // then
        assertEquals(johnUserEntity, edyUserEntity);
    }

}