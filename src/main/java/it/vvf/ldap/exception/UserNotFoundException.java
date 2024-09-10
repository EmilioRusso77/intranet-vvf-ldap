package it.vvf.ldap.exception;

public class UserNotFoundException extends UserRoleException {
    public UserNotFoundException(String username) {
        super("Utente non trovato: " + username);
    }
}
