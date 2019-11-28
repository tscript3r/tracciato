package pl.tscript3r.tracciato.user;

import org.mindrot.jbcrypt.BCrypt;

final class BCryptPasswordEncrypt implements PasswordEncrypt {

    @Override
    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean checkPassword(String candidate, String password) {
        return BCrypt.checkpw(candidate, password);
    }

}