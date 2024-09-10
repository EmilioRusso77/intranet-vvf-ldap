package it.vvf.ldap.dto;

import java.util.ArrayList;
import java.util.List;

import it.vvf.ldap.util.Section;
import it.vvf.ldap.util.UserRole;
import lombok.Data;

@Data
public class UserRoleDto {

    private String username;
    UserRole role;
    List<Section> sections = new ArrayList<>();
}
