package it.vvf.ldap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.vvf.ldap.dto.UserRoleDto;
import it.vvf.ldap.service.ConfirmationCodeService;
import it.vvf.ldap.service.UserRoleService;
import it.vvf.ldap.util.Section;
import it.vvf.ldap.util.UserRole;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "API per la gestione degli utenti, ruoli e sezioni")
public class UserRoleController {

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private ConfirmationCodeService confirmationCodeService;

	// CRUD Utente
	@Operation(summary = "Crea un nuovo utente")
	@ApiResponse(responseCode = "201", description = "Utente creato con successo")
	@ApiResponse(responseCode = "400", description = "Dati di input non validi")
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@RequestBody UserRoleDto userRole) {
		try {
			UserRoleDto createdUser = userRoleService.addUserRole(userRole);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Ottiene un utente per username")
	@ApiResponse(responseCode = "200", description = "Utente trovato")
	@ApiResponse(responseCode = "404", description = "Utente non trovato")
	@GetMapping("/users/{username}")
	public ResponseEntity<UserRoleDto> getUser(@PathVariable String username) {
		try {
			UserRoleDto user = userRoleService.getUserRoleByUsername(username);
			return ResponseEntity.ok(user);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Aggiorna un utente esistente")
	@ApiResponse(responseCode = "200", description = "Utente aggiornato con successo")
	@ApiResponse(responseCode = "404", description = "Utente non trovato")
	@PutMapping("/users/user")
	public ResponseEntity<?> updateUser(@RequestBody UserRoleDto userRole) {
		try {
			UserRoleDto updatedUser = userRoleService.updateUserRole(userRole);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Elimina un utente")
	@ApiResponse(responseCode = "204", description = "Utente eliminato con successo")
	@ApiResponse(responseCode = "404", description = "Utente non trovato")
	@DeleteMapping("/users/{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		try {
			userRoleService.deleteUserRole(username);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Ottiene tutti gli utenti")
	@ApiResponse(responseCode = "200", description = "Lista di tutti gli utenti")
	@GetMapping("/users")
	public ResponseEntity<List<UserRoleDto>> getAllUsers() {
		List<UserRoleDto> users = userRoleService.getAllUserRoles();
		return ResponseEntity.ok(users);
	}

	// CRUD Ruolo
	@Operation(summary = "Imposta il ruolo di un utente")
	@ApiResponse(responseCode = "200", description = "Ruolo impostato con successo")
	@ApiResponse(responseCode = "404", description = "Utente non trovato")
	@PutMapping("/users/{username}/role")
	public ResponseEntity<?> setUserRole(@PathVariable String username,
			@RequestBody @Parameter(description = "Nuovo ruolo", required = true) UserRole role) {
		try {
			UserRoleDto updatedUser = userRoleService.setUserRole(username, role);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Ottiene il ruolo di un utente")
	@ApiResponse(responseCode = "200", description = "Ruolo dell'utente")
	@ApiResponse(responseCode = "404", description = "Utente non trovato")
	@GetMapping("/users/{username}/role")
	public ResponseEntity<UserRole> getUserRole(@PathVariable String username) {
		try {
			UserRole role = userRoleService.getUserRole(username);
			return ResponseEntity.ok(role);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Ottiene tutti i ruoli disponibili")
	@ApiResponse(responseCode = "200", description = "Lista dei ruoli disponibili")
	@GetMapping("/users/roles")
	public ResponseEntity<UserRole[]> getAvailableRoles() {
		return ResponseEntity.ok(UserRole.values());
	}

	// CRUD Sezioni
	@Operation(summary = "Aggiunge una sezione a un utente")
	@ApiResponse(responseCode = "200", description = "Sezione aggiunta con successo")
	@ApiResponse(responseCode = "400", description = "Sezione non valida")
	@PostMapping("/users/{username}/sections")
	public ResponseEntity<?> addSectionToUser(@PathVariable String username,
			@RequestBody @Parameter(description = "Sezione da aggiungere", required = true) Section section) {
		try {
			UserRoleDto updatedUser = userRoleService.addSectionToUser(username, section);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Rimuove una sezione da un utente")
	@ApiResponse(responseCode = "200", description = "Sezione rimossa con successo")
	@ApiResponse(responseCode = "404", description = "Utente o sezione non trovata")
	@DeleteMapping("/users/{username}/sections/{section}")
	public ResponseEntity<?> removeSectionFromUser(@PathVariable String username, @PathVariable Section section) {
		try {
			UserRoleDto updatedUser = userRoleService.removeSectionFromUser(username, section);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Ottiene tutte le sezioni di un utente")
	@ApiResponse(responseCode = "200", description = "Lista delle sezioni dell'utente")
	@ApiResponse(responseCode = "404", description = "Utente non trovato")
	@GetMapping("/users/{username}/sections")
	public ResponseEntity<List<Section>> getUserSections(@PathVariable String username) {
		try {
			List<Section> sections = userRoleService.getUserSections(username);
			return ResponseEntity.ok(sections);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Imposta tutte le sezioni di un utente")
	@ApiResponse(responseCode = "200", description = "Sezioni impostate con successo")
	@ApiResponse(responseCode = "400", description = "Dati di input non validi")
	@PutMapping("/users/{username}/sections")
	public ResponseEntity<?> setUserSections(@PathVariable String username, @RequestBody List<Section> sections) {
		try {
			UserRoleDto updatedUser = userRoleService.setUserSections(username, sections);
			return ResponseEntity.ok(updatedUser);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Ottiene tutte le sezioni disponibili")
	@ApiResponse(responseCode = "200", description = "Lista delle sezioni disponibili")
	@GetMapping("/users/sections")
	public ResponseEntity<Section[]> getAvailableSections() {
		return ResponseEntity.ok(Section.getAllSections());
	}

	@Operation(summary = "Richiedi codice di conferma per l'eliminazione di massa")
	@ApiResponse(responseCode = "200", description = "Codice di conferma generato")
	@GetMapping("/users/delete-all-confirmation-code")
	public ResponseEntity<String> requestDeleteAllConfirmationCode() {
		String code = confirmationCodeService.generateConfirmationCode();
		return ResponseEntity.ok("Codice di conferma: " + code + ". Questo codice scadrà tra 5 minuti.");
	}

	@Operation(summary = "Elimina tutti gli utenti")
	@ApiResponse(responseCode = "200", description = "Tutti gli utenti sono stati eliminati")
	@ApiResponse(responseCode = "400", description = "Codice di conferma non valido o scaduto")
	@DeleteMapping("/users")
	public ResponseEntity<String> deleteAllUsers(@RequestParam String confirmationCode) {
		try {
			long deletedCount = userRoleService.deleteAllUserRoles(confirmationCode);
			return ResponseEntity.ok("Eliminati " + deletedCount + " utenti");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
