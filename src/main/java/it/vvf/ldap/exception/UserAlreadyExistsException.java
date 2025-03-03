package it.vvf.ldap.exception;

public class UserAlreadyExistsException extends UserRoleException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4458766570395611356L;

	public UserAlreadyExistsException(String username) {
        super("Utente già esistente: " + username);
    }
}
