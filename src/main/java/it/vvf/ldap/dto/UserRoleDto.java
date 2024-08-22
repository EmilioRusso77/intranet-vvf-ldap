package it.vvf.ldap.dto;

import it.vvf.ldap.util.UserRole;
import lombok.Data;

@Data
public class UserRoleDto {

    private String username;
    UserRole role;

}
