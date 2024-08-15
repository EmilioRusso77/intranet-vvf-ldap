package it.vvf.ldap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapClient;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import java.util.List;

@Service
@Slf4j
public class LdapService {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private LdapContextSource contextSource;


	public List<String> getAllUsernames() {
		return ldapTemplate.search("", "(objectclass=person)", new AttributesMapper<String>() {
			@Override
			public String mapFromAttributes(Attributes attributes) throws NamingException {
				return attributes.get("cn").get().toString();
			}
		});
	}

	public String findUserByUsername(String username) {
		String searchFilter = "(userPrincipalName=" + username + ")";

		List<String> results = ldapTemplate.search("", searchFilter,
				(AttributesMapper<String>) attributes -> attributes.get("cn").get().toString());

		return results.isEmpty() ? null : results.get(0);
	}
	
	
	public String findUserDnByUsername(String username) {
        String searchFilter = "(userPrincipalName=" + username + ")";
        List<String> results = ldapTemplate.search(
            "",
            searchFilter,
            (AttributesMapper<String>) attributes -> attributes.get("distinguishedName").get().toString()
        );

        return results.isEmpty() ? null : results.get(0);
    }

    public boolean authenticate(String username, String password) throws Exception {
        try {
            String userDn = findUserDnByUsername(username);
            if (userDn == null) {
                throw new Exception("User not found");
            }
            ldapTemplate.getContextSource().getContext(userDn, password);
            return true;
        } catch (Exception e) {
            throw new Exception("Invalid credentials", e);
        }
    }

    public void printUserAttributes(String username) {
    	  String searchFilter = "(userPrincipalName=" + username + ")";
          List<String> results = ldapTemplate.search(
              "",
              searchFilter,
              (AttributesMapper<String>) attributes -> attributes.get("distinguishedName").get().toString()
          );
          if(results != null && !results.isEmpty()) {
        	  for (String string : results) {
				System.err.println(string);
			}
          }
    }

	
}
