package it.vvf.ldap.exception;

public class UserAlreadyExistsException extends UserRoleException {
    public UserAlreadyExistsException(String username) {
        super("Utente già esistente: " + username);
    }
}
