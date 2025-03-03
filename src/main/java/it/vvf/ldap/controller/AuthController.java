package it.vvf.ldap.controller;

import javax.management.RuntimeErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.vvf.ldap.dto.UserRoleDto;
import it.vvf.ldap.service.CustomUserDetailsService;
import it.vvf.ldap.service.UserRoleService;
import it.vvf.ldap.util.ApiResponse;
import it.vvf.ldap.util.JwtAuthenticationResponse;
import it.vvf.ldap.util.JwtUtil;
import it.vvf.ldap.util.LDAPConstants;
import it.vvf.ldap.util.LoginRequest;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
@Tag(name = "Auth Management", description = "API per la gestione del login su Ldap Nazionale")
public class AuthController {

    
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRoleService userRoleService;
    
    public AuthController(AuthenticationManager authenticationManager,CustomUserDetailsService customUserDetailsService,JwtUtil jwtUtil,UserRoleService userRoleService) {
    
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRoleService = userRoleService;
        
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/generatetoken")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws Exception {
    	authenticate(loginRequest.getUsername(), loginRequest.getPassword());
    	if(loginRequest.getUsername() == null ) {
    		 return new ResponseEntity(new ApiResponse(false, LDAPConstants.USERNAME_OR_PASSWORD_INVALID),
                     HttpStatus.BAD_REQUEST);
    	}
    	
		 UserDetails userDetails=customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
		 UserRoleDto userRole = userRoleService.loadUserByUsername(userDetails.getUsername());
		 String jwt=jwtUtil.generateToken(userRole);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    
    
    private void authenticate(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException(LDAPConstants.USER_DISABLED, e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(LDAPConstants.INVALID_CREDENTIALS, e);
		}catch (LockedException e) {
			throw new LockedException(LDAPConstants.LOCKED_CREDENTIALS, e);
		}
		catch (RuntimeErrorException e) {
			Error error = new Error(LDAPConstants.INTERNAL_SERVER_ERROR	);
			throw new RuntimeErrorException(error, e.getMessage());
		}
	}
}
