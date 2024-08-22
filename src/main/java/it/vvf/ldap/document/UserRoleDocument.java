package it.vvf.ldap.document;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import it.vvf.ldap.util.UserRole;
import lombok.Data;

@Data
@Document(collection = "users")
public class UserRoleDocument {

	@MongoId
    private String id;
    private String username;
    UserRole role;
}
