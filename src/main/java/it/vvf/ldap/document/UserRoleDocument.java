package it.vvf.ldap.document;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import it.vvf.ldap.util.Section;
import it.vvf.ldap.util.UserRole;
import lombok.Data;

@Data
@Document(collection = "users")
public class UserRoleDocument {

	@MongoId
    private String id;
    private String username;
    UserRole role;
    List<Section> sections = new ArrayList<>();
}
