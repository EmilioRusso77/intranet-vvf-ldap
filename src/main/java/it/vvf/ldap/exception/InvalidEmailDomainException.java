package it.vvf.ldap.exception;

public class InvalidEmailDomainException extends UserRoleException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1136266942731664003L;

	public InvalidEmailDomainException(String email) {
        super("Dominio email non valido: " + email);
    }
}
