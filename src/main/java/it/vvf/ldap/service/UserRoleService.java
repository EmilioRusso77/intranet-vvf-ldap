package it.vvf.ldap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.vvf.ldap.document.UserRoleDocument;
import it.vvf.ldap.dto.UserRoleDto;
import it.vvf.ldap.exception.InvalidEmailDomainException;
import it.vvf.ldap.exception.InvalidSectionException;
import it.vvf.ldap.exception.UserAlreadyExistsException;
import it.vvf.ldap.exception.UserNotFoundException;
import it.vvf.ldap.service.repository.UserRoleRepository;
import it.vvf.ldap.util.Section;
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
				BeanUtils.copyProperties(userRoleDocument, userRoleDto);
				return userRoleDto;
		}
    // Primo accesso utente role: USER e sections: [COMMMON]
		userRoleDto.setRole(UserRole.valueOf("USER"));
		List<Section> sections = new ArrayList<>();
		sections.add(Section.valueOf("COMMON"));
		userRoleDto.setSections(sections);
		return userRoleDto;

	}

	public UserRoleDto addUserRole(UserRoleDto userRoleDto) {
		validateEmailDomain(userRoleDto.getUsername()); // Verifica il dominio dell'email
		 if (userRoleRepository.findByUsername(userRoleDto.getUsername()).isPresent()) {
	            throw new UserAlreadyExistsException(userRoleDto.getUsername());
	        }
		UserRoleDocument userRoleDocument =  new UserRoleDocument();
		BeanUtils.copyProperties(userRoleDto, userRoleDocument);
		userRoleDocument = userRoleRepository.save(userRoleDocument);
		BeanUtils.copyProperties(userRoleDocument, userRoleDto);
		return userRoleDto;
	}
	
	public UserRoleDto updateUserRole(UserRoleDto userRoleDto) {
		validateEmailDomain(userRoleDto.getUsername()); // Verifica il dominio dell'email
		UserRoleDocument existingUserRoleDocument = userRoleRepository.findByUsername(userRoleDto.getUsername())
	            .orElseThrow(() -> new UserNotFoundException(userRoleDto.getUsername()));

	    // Aggiorna i campi esistenti con quelli forniti nel DTO
	    BeanUtils.copyProperties(userRoleDto, existingUserRoleDocument, "id");

	    existingUserRoleDocument = userRoleRepository.save(existingUserRoleDocument);

	    BeanUtils.copyProperties(existingUserRoleDocument, userRoleDto);
	    return userRoleDto;
	}
	
	public void deleteUserRole(String username) {
		validateEmailDomain(username); // Verifica il dominio dell'email
		UserRoleDocument userRoleDocument = userRoleRepository.findByUsername(username)
	            .orElseThrow(() -> new UserNotFoundException(username));

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
            throw new InvalidEmailDomainException(email);
        }
    }
    
    public UserRoleDto addSectionToUser(String username, Section section) {
        validateEmailDomain(username);
        UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
        if (!Section.isValidSection(section)) {
            throw new InvalidSectionException(section.toString());
        }
        if (!userRoleDocument.getSections().contains(section)) {
            userRoleDocument.getSections().add(section);
            userRoleDocument = userRoleRepository.save(userRoleDocument);
        }
        return convertToDto(userRoleDocument);
    }

    public UserRoleDto removeSectionFromUser(String username, Section section) {
        validateEmailDomain(username);
        UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
        userRoleDocument.getSections().remove(section);
        userRoleDocument = userRoleRepository.save(userRoleDocument);
        return convertToDto(userRoleDocument);
    }

    public List<Section> getUserSections(String username) {
        validateEmailDomain(username);
        UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
        return userRoleDocument.getSections();
    }

    public UserRoleDto setUserSections(String username, List<Section> sections) {
        validateEmailDomain(username);
        UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
        userRoleDocument.setSections(sections);
        userRoleDocument = userRoleRepository.save(userRoleDocument);
        return convertToDto(userRoleDocument);
    }

    private UserRoleDocument getUserRoleDocumentByUsername(String username) {
        return userRoleRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private UserRoleDto convertToDto(UserRoleDocument document) {
        UserRoleDto dto = new UserRoleDto();
        BeanUtils.copyProperties(document, dto);
        return dto;
    }
}
