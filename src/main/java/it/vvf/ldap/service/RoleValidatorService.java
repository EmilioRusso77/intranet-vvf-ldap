package it.vvf.ldap.service;

import org.springframework.stereotype.Service;

import it.vvf.ldap.util.UserRole;

import java.util.Arrays;

@Service
public class RoleValidatorService {

    public boolean isValidRole(String role) {
        return Arrays.stream(UserRole.values())
                     .anyMatch(enumRole -> enumRole.name().equals(role));
    }
}
