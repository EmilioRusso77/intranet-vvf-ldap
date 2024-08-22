package it.vvf.ldap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.vvf.ldap.document.UserRoleDocument;
import it.vvf.ldap.dto.UserRoleDto;
import it.vvf.ldap.service.repository.UserRoleRepository;
import it.vvf.ldap.util.UserRole;

@Service
public class UserRoleService {

	@Autowired
	private UserRoleRepository userRoleRepository;
	private static final String EMAIL_DOMAIN = "@dipvvf.it";

	// Verifica il ruolo di un utente basato sul nome utente
	public UserRoleDto loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserRoleDocument> userRoleOptional = userRoleRepository.findByUsername(username);
		UserRoleDto userRoleDto = new UserRoleDto();
		if (userRoleOptional.isPresent()) {
			UserRoleDocument userRoleDocument = userRoleOptional.get();

			// Se l'utente è presente e ha il ruolo di "ADMIN"
			if ("ADMIN".equals(userRoleDocument.getRole().toString())) {

				BeanUtils.copyProperties(userRoleDocument, userRoleDto);
				return userRoleDto;
			}

		}
		// Se l'utente non è presente nel database, gli viene assegnato il ruolo di
		// "USER" di default
		userRoleDto.setRole(UserRole.valueOf("USER"));
		return userRoleDto;

	}

	public UserRoleDto addUserRole(UserRoleDto userRoleDto) {
		validateEmailDomain(userRoleDto.getUsername()); // Verifica il dominio dell'email
		if (userRoleRepository.findByUsername(userRoleDto.getUsername()).isPresent()) {
			throw new RuntimeException("L'utente con username " + userRoleDto.getUsername() + " esiste già.");
		}
		UserRoleDocument userRoleDocument =  new UserRoleDocument();
		BeanUtils.copyProperties(userRoleDto, userRoleDocument);
		userRoleDocument = userRoleRepository.save(userRoleDocument);
		BeanUtils.copyProperties(userRoleDocument, userRoleDto);
		return userRoleDto;
	}
	
	public UserRoleDto updateUserRole(String username, UserRoleDto userRoleDto) {
		validateEmailDomain(userRoleDto.getUsername()); // Verifica il dominio dell'email
	    UserRoleDocument existingUserRoleDocument = userRoleRepository.findByUsername(username)
	        .orElseThrow(() -> new RuntimeException("L'utente con username " + username + " non esiste."));

	    // Aggiorna i campi esistenti con quelli forniti nel DTO
	    BeanUtils.copyProperties(userRoleDto, existingUserRoleDocument, "id");

	    existingUserRoleDocument = userRoleRepository.save(existingUserRoleDocument);

	    BeanUtils.copyProperties(existingUserRoleDocument, userRoleDto);
	    return userRoleDto;
	}
	
	public void deleteUserRole(String username) {
		validateEmailDomain(username); // Verifica il dominio dell'email
	    UserRoleDocument userRoleDocument = userRoleRepository.findByUsername(username)
	        .orElseThrow(() -> new RuntimeException("L'utente con username " + username + " non esiste."));

	    userRoleRepository.delete(userRoleDocument);
	}

	 // Metodo per ottenere un ruolo utente per username
    public UserRoleDto getUserRoleByUsername(String username) {
    	validateEmailDomain(username); // Verifica il dominio dell'email
        UserRoleDocument userRoleDocument = userRoleRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Ruolo utente non trovato per username: " + username));
        UserRoleDto userRoleDto = new UserRoleDto();
        BeanUtils.copyProperties(userRoleDocument, userRoleDto);
        return userRoleDto;
    }

    // Metodo per ottenere tutti i ruoli utente
    public List<UserRoleDto> getAllUserRoles() {
        List<UserRoleDocument> userRoleDocuments = userRoleRepository.findAll();
        List<UserRoleDto> userRoleDtos = new ArrayList<>();
        for (UserRoleDocument userRoleDocument : userRoleDocuments) {
            UserRoleDto userRoleDto = new UserRoleDto();
            BeanUtils.copyProperties(userRoleDocument, userRoleDto);
            userRoleDtos.add(userRoleDto);
        }
        return userRoleDtos;
    }


    private void validateEmailDomain(String email) {
        if (!email.endsWith(EMAIL_DOMAIN)) {
            throw new RuntimeException("L'email deve avere il dominio " + EMAIL_DOMAIN);
        }
    }
}
