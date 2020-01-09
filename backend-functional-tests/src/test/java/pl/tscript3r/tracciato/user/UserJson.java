package pl.tscript3r.tracciato.user;

import pl.tscript3r.tracciato.AbstractJson;
import pl.tscript3r.tracciato.user.api.UserDto;

import static pl.tscript3r.tracciato.user.UserConst.*;

public class UserJson extends AbstractJson<UserDto> {

    public static final String NEW_USERNAME = EDY_USERNAME;
    public static final String NEW_PASSWORD = EDY_PASSWORD;
    public static final String NEW_EMAIL = EDY_EMAIL;

    public static final String EXISTING_USERNAME = JOHNS_USERNAME;
    public static final String EXISTING_PASSWORD = JOHNS_PASSWORD;
    public static final String EXISTING_EMAIL = JOHNS_EMAIL;

    public static UserJson newValid() {
        return new UserJson(UserConst.getValidEdyUserDto());
    }

    public static UserJson existing() {
        return new UserJson(UserConst.getValidJohnsUserDto());
    }

    public UserJson(UserDto userDto) {
        super(userDto);
    }

    public UserJson email(String email) {
        object.setEmail(email);
        return this;
    }

    public UserJson username(String username) {
        object.setUsername(username);
        return this;
    }

    public UserJson password(String password) {
        object.setPassword(password);
        return this;
    }

    public String getUsername() {
        return object.getUsername();
    }
}
