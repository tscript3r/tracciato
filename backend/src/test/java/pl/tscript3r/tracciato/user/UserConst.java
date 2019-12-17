package pl.tscript3r.tracciato.user;

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

    private UserConst() {
    }

}
