package it.vvf.ldap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Autowired
    private ConfirmationCodeService confirmationCodeService;
	
	private static final String EMAIL_DOMAIN = "@dipvvf.it";

	// Verifica il ruolo di un utente basato sul nome utente
	public UserRoleDto loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserRoleDocument> userRoleOptional = userRoleRepository.findByUsername(username);
		UserRoleDto userRoleDto = new UserRoleDto();
		UserRoleDocument userRoleDocument = new UserRoleDocument();
		if (userRoleOptional.isPresent()) {
			userRoleDocument = userRoleOptional.get();
			BeanUtils.copyProperties(userRoleDocument, userRoleDto);
			return userRoleDto;
		}
		// Primo accesso utente role: USER e sections: [COMMMON]
		userRoleDocument.setUsername(username);
		userRoleDocument.setRole(UserRole.valueOf("USER"));
		List<Section> sections = new ArrayList<>();
		sections.add(Section.valueOf("COMMON"));
		userRoleDocument.setSections(sections);
		userRoleDocument = userRoleRepository.save(userRoleDocument);
		BeanUtils.copyProperties(userRoleDocument, userRoleDto);
		return userRoleDto;

	}

	// CRUD Utente
	public UserRoleDto addUserRole(UserRoleDto userRoleDto) {
		validateEmailDomain(userRoleDto.getUsername());
		if (userRoleRepository.findByUsername(userRoleDto.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException(userRoleDto.getUsername());
		}
		UserRoleDocument userRoleDocument = new UserRoleDocument();
		BeanUtils.copyProperties(userRoleDto, userRoleDocument);
		userRoleDocument = userRoleRepository.save(userRoleDocument);
		return convertToDto(userRoleDocument);
	}

	public UserRoleDto getUserRoleByUsername(String username) {
		validateEmailDomain(username);
		UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
		return convertToDto(userRoleDocument);
	}

	public UserRoleDto updateUserRole(UserRoleDto userRoleDto) {
		validateEmailDomain(userRoleDto.getUsername());
		UserRoleDocument existingUserRoleDocument = getUserRoleDocumentByUsername(userRoleDto.getUsername());
		BeanUtils.copyProperties(userRoleDto, existingUserRoleDocument, "id");
		existingUserRoleDocument = userRoleRepository.save(existingUserRoleDocument);
		return convertToDto(existingUserRoleDocument);
	}

	public void deleteUserRole(String username) {
		validateEmailDomain(username);
		UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
		userRoleRepository.delete(userRoleDocument);
	}

	public List<UserRoleDto> getAllUserRoles() {
		List<UserRoleDocument> userRoleDocuments = userRoleRepository.findAll();
		return userRoleDocuments.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	// CRUD Ruolo
	public UserRoleDto setUserRole(String username, UserRole role) {
		validateEmailDomain(username);
		UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
		userRoleDocument.setRole(role);
		userRoleDocument = userRoleRepository.save(userRoleDocument);
		return convertToDto(userRoleDocument);
	}

	public UserRole getUserRole(String username) {
		validateEmailDomain(username);
		UserRoleDocument userRoleDocument = getUserRoleDocumentByUsername(username);
		return userRoleDocument.getRole();
	}

	// CRUD Sezioni
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

	// Metodi di utilità
	private void validateEmailDomain(String email) {
		if (!email.endsWith(EMAIL_DOMAIN)) {
			throw new InvalidEmailDomainException(email);
		}
	}

	private UserRoleDocument getUserRoleDocumentByUsername(String username) {
		return userRoleRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
	}

	private UserRoleDto convertToDto(UserRoleDocument document) {
		UserRoleDto dto = new UserRoleDto();
		BeanUtils.copyProperties(document, dto);
		return dto;
	}
	
	
	
	 /**
     * Elimina tutti gli utenti dal database.
     * Richiede una conferma per motivi di sicurezza.
     *
     * @param confirmationCode Codice di conferma per autorizzare l'operazione
     * @return Il numero di utenti eliminati
     * @throws IllegalArgumentException se il codice di conferma non è corretto
     */
	@Transactional
    public long deleteAllUserRoles(String confirmationCode) {
        if (!confirmationCodeService.isValidCode(confirmationCode)) {
            throw new IllegalArgumentException("Codice di conferma non valido o scaduto");
        }
        long count = userRoleRepository.count();
        userRoleRepository.deleteAll();
        return count;
    }
}