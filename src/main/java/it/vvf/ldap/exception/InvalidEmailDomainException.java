package it.vvf.ldap.exception;

public class InvalidEmailDomainException extends UserRoleException {
    public InvalidEmailDomainException(String email) {
        super("Dominio email non valido: " + email);
    }
}
