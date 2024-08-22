package it.vvf.ldap.service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.vvf.ldap.document.UserRoleDocument;

public interface UserRoleRepository extends MongoRepository<UserRoleDocument, String> {
    Optional<UserRoleDocument> findByUsername(String username);
}
