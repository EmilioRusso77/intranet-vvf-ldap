package it.vvf.ldap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LdapService {

	private static final String USER_PRINCIPAL_NAME = "userPrincipalName";
	private static final String DISTINGUISHED_NAME = "distinguishedName";
	
	@Autowired
	private LdapTemplate ldapTemplate;

	public void printUserAttributes(String username) {
		String searchFilter = "(" + USER_PRINCIPAL_NAME + "=" + username + ")";
		List<String> results = ldapTemplate.search("", searchFilter,
				(AttributesMapper<String>) attributes -> attributes.get(DISTINGUISHED_NAME).get().toString());
		if (results != null && !results.isEmpty()) {
			for (String string : results) {
				log.error(string);
			}
		}
	}

}
