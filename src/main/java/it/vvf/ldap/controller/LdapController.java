package it.vvf.ldap.controller;

import it.vvf.ldap.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ldap")
public class LdapController {

    @Autowired
    private LdapService ldapService;

    @GetMapping("/users")
    public List<String> getAllUsernames() {
        return ldapService.getAllUsernames();
    }
    

    @GetMapping("/user/{username}")
    public String getUserByUsername(@PathVariable String username) {
        return ldapService.findUserByUsername(username);
    }
    
    
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam String username, @RequestParam String password) throws Exception {
        boolean isAuthenticated = ldapService.authenticate(username, password);
        return isAuthenticated ? ResponseEntity.ok("Authenticated") : ResponseEntity.status(401).body("Authentication failed");
    }
    
    @GetMapping("/user/print/{username}")
    public ResponseEntity<Void> printUserByUsername(@PathVariable String username) {
        ldapService.printUserAttributes(username);
        return ResponseEntity.ok().build();
    }
}
