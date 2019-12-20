package pl.tscript3r.tracciato.user;

import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;

public final class UserConst {

    public static final String JOHNS_USERNAME = "John";
    public static final String JOHNS_EMAIL = "johns@email.com";
    public static final String JOHNS_PASSWORD = "johnsPassword";
    public static final UUID JOHNS_UUID = UUID.randomUUID();
    public static final String EDY_USERNAME = "Edy666";
    public static final String EDY_EMAIL = "edys@mail.com";
    public static final String EDY_PASSWORD = "edysPassword";
    public static final UUID EDY_UUID = UUID.randomUUID();
    public static final long JOHNS_ID = 1L;
    public static final long EDY_ID = 2L;

    public static final String USERNAME_FIELD = "username";

    public static UserEntity getJohnUserEntity() {
        var user = new UserEntity();
        user.setId(JOHNS_ID);
        user.setUsername(JOHNS_USERNAME);
        user.setEmail(JOHNS_EMAIL);
        user.setPassword(JOHNS_PASSWORD);
        user.setUuid(JOHNS_UUID);
        return user;
    }

    public static UserEntity getEdyUserEntity() {
        var user = new UserEntity();
        user.setId(EDY_ID);
        user.setUsername(EDY_USERNAME);
        user.setEmail(EDY_EMAIL);
        user.setPassword(EDY_PASSWORD);
        user.setUuid(EDY_UUID);
        return user;
    }

    public static UserDto getValidJohnsUserDto() {
        UserDto validUserDto = new UserDto();
        validUserDto.setEmail(JOHNS_EMAIL);
        validUserDto.setUsername(JOHNS_USERNAME);
        validUserDto.setPassword(JOHNS_PASSWORD);
        return validUserDto;
    }

    public static UserDto getValidEdyUserDto() {
        UserDto validUserDto = new UserDto();
        validUserDto.setEmail(EDY_EMAIL);
        validUserDto.setUsername(EDY_USERNAME);
        validUserDto.setPassword(EDY_PASSWORD);
        return validUserDto;
    }

    private UserConst() {
    }

}
