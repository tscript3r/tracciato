package pl.tscript3r.tracciato.user;

interface PasswordEncrypt {

    String encryptPassword(String password);

    boolean checkPassword(String candidate, String password);

}