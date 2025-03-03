package it.vvf.ldap.exception;

public class InvalidSectionException extends UserRoleException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6809413904443103198L;

	public InvalidSectionException(String section) {
        super("Sezione non valida: " + section);
    }
}
