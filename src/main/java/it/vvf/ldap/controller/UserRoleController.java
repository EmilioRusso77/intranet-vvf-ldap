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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import it.vvf.ldap.dto.UserRoleDto;
import it.vvf.ldap.service.UserRoleService;
import it.vvf.ldap.util.Section;
import it.vvf.ldap.util.UserRole;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Role Management", description = "API per la gestione dei ruoli utente e delle sezioni")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    // Metodo per aggiungere un nuovo ruolo utente
    @Operation(summary = "Aggiunge un nuovo ruolo utente")
    @ApiResponse(responseCode = "200", description = "Ruolo utente aggiunto con successo")
    @ApiResponse(responseCode = "400", description = "Dati di input non validi")
    @PostMapping("/role")
    public ResponseEntity<?> addUserRole(
            @RequestBody @Parameter(description = "Dettagli del ruolo utente", required = true) UserRoleDto userRole) {
        try {
            UserRoleDto userRoleDto = userRoleService.addUserRole(userRole);
            return ResponseEntity.ok(userRoleDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Metodo per aggiornare un ruolo utente esistente
    @Operation(summary = "Aggiorna un ruolo utente esistente")
    @ApiResponse(responseCode = "200", description = "Ruolo utente aggiornato con successo")
    @ApiResponse(responseCode = "404", description = "Utente non trovato")
    @PutMapping("/role/{username}")
    public ResponseEntity<?> updateUserRole(@RequestBody @Parameter(description = "Dettagli aggiornati del ruolo utente", required = true) UserRoleDto userRole) {
        try {
            UserRoleDto updatedUserRoleDto = userRoleService.updateUserRole(userRole);
            return ResponseEntity.ok(updatedUserRoleDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Metodo per eliminare un ruolo utente
    @DeleteMapping("/role/{username}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable String username) {
        try {
            userRoleService.deleteUserRole(username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Metodo per ottenere un ruolo utente per username
    @GetMapping("/role/{username}")
    public ResponseEntity<UserRoleDto> getUserRoleByUsername(@PathVariable String username) {
        try {
            UserRoleDto userRoleDto = userRoleService.getUserRoleByUsername(username);
            return ResponseEntity.ok(userRoleDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Metodo per ottenere tutti i ruoli utente
    @GetMapping("/roles")
    public ResponseEntity<List<UserRoleDto>> getAllUserRoles() {
        try {
            List<UserRoleDto> userRoles = userRoleService.getAllUserRoles();
            return ResponseEntity.ok(userRoles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @Operation(summary = "Ottiene tutti i ruoli utente disponibili")
    @ApiResponse(responseCode = "200", description = "Lista dei ruoli utente")
    @GetMapping("/roles/available")
    public ResponseEntity<UserRole[]> getAvailableRoles() {
        return ResponseEntity.ok(UserRole.values());
    }
    
    @Operation(summary = "Ottiene tutte le sezioni disponibili")
    @ApiResponse(responseCode = "200", description = "Lista delle sezioni")
    @GetMapping("/sections/available")
    public ResponseEntity<Section[]> getAvailableSections() {
        return ResponseEntity.ok(Section.values());
    }
    
    // Metodo per aggiungere una sezione a un utente
    @Operation(summary = "Aggiunge una sezione a un utente")
    @ApiResponse(responseCode = "200", description = "Sezione aggiunta con successo")
    @ApiResponse(responseCode = "400", description = "Sezione non valida")
    @PostMapping("/role/{username}/sections")
    public ResponseEntity<?> addSectionToUser(
            @PathVariable @Parameter(description = "Username dell'utente", required = true) String username,
            @RequestBody @Parameter(description = "Sezione da aggiungere", required = true, schema = @Schema(implementation = Section.class)) Section section) {
        try {
            UserRoleDto updatedUserRole = userRoleService.addSectionToUser(username, section);
            return ResponseEntity.ok(updatedUserRole);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Metodo per rimuovere una sezione da un utente
    @DeleteMapping("/role/{username}/sections/{section}")
    public ResponseEntity<?> removeSectionFromUser(@PathVariable String username, @PathVariable Section section) {
        try {
            UserRoleDto updatedUserRole = userRoleService.removeSectionFromUser(username, section);
            return ResponseEntity.ok(updatedUserRole);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Metodo per ottenere tutte le sezioni di un utente
    @GetMapping("/role/{username}/sections")
    public ResponseEntity<List<Section>> getUserSections(@PathVariable String username) {
        try {
            List<Section> sections = userRoleService.getUserSections(username);
            return ResponseEntity.ok(sections);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Metodo per impostare tutte le sezioni di un utente
    @PutMapping("/role/{username}/sections")
    public ResponseEntity<?> setUserSections(@PathVariable String username, @RequestBody List<Section> sections) {
        try {
            UserRoleDto updatedUserRole = userRoleService.setUserSections(username, sections);
            return ResponseEntity.ok(updatedUserRole);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


