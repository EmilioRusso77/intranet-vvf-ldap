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

import it.vvf.ldap.dto.UserRoleDto;
import it.vvf.ldap.service.UserRoleService;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    // Metodo per aggiungere un nuovo ruolo utente
    @PostMapping("/role")
    public ResponseEntity<?> addUserRole(@RequestBody UserRoleDto userRole) {
        try {
            UserRoleDto userRoleDto = userRoleService.addUserRole(userRole);
            return ResponseEntity.ok(userRoleDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Metodo per aggiornare un ruolo utente esistente
    @PutMapping("/role/{username}")
    public ResponseEntity<?> updateUserRole(@PathVariable String username, @RequestBody UserRoleDto userRole) {
        try {
            UserRoleDto updatedUserRoleDto = userRoleService.updateUserRole(username, userRole);
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
}


