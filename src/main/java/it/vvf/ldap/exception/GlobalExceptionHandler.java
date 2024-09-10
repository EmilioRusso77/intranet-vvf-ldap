package it.vvf.ldap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.vvf.ldap.util.LDAPConstants;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


	@ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ErrorResponse(LDAPConstants.USER_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorResponse(LDAPConstants.INVALID_CREDENTIALS, ex.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleDisabledException(DisabledException ex) {
        return new ErrorResponse(LDAPConstants.USER_ACCOUNT_IS_DISABLED, ex.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleLockedException(LockedException ex) {
        return new ErrorResponse(LDAPConstants.USER_ACCOUNT_IS_LOCKED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleGeneralException(Exception ex) {
        return new ErrorResponse(LDAPConstants.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("UserAlreadyExistsException: ", ex);
        return new ErrorResponse("USER_ALREADY_EXISTS", "L'utente esiste già.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
    	log.error("UserNotFoundException: ", ex);
        return new ErrorResponse("USER_NOT_FOUND", "Utente non trovato.");
    }

    @ExceptionHandler(InvalidEmailDomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidEmailDomainException(InvalidEmailDomainException ex) {
    	log.error("InvalidEmailDomainException: ", ex);
        return new ErrorResponse("INVALID_EMAIL_DOMAIN", "Dominio email non valido.");
    }

    @ExceptionHandler(InvalidSectionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidSectionException(InvalidSectionException ex) {
    	log.error("InvalidSectionException: ", ex);
        return new ErrorResponse("INVALID_SECTION", "Sezione non valida.");
    }

    @ExceptionHandler(UserRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleUserRoleException(UserRoleException ex) {
    	log.error("UserRoleException: ", ex);
        return new ErrorResponse("USER_ROLE_ERROR", "Errore nella gestione del ruolo utente.");
    }


    // Classe di risposta per gli errori
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        // Getter e Setter
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

