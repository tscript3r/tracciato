package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.user.UserConst.getEdyUserEntity;
import static pl.tscript3r.tracciato.user.UserConst.getJohnUserEntity;

@DisplayName("User entity")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class UserEntityTest {

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