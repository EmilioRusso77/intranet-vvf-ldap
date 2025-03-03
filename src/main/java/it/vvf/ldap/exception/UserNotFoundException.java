package it.vvf.ldap.exception;

public class UserNotFoundException extends UserRoleException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5305037756768765998L;

	public UserNotFoundException(String username) {
        super("Utente non trovato: " + username);
    }
}
