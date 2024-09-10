package it.vvf.ldap.exception;

public class InvalidSectionException extends UserRoleException {
    public InvalidSectionException(String section) {
        super("Sezione non valida: " + section);
    }
}
