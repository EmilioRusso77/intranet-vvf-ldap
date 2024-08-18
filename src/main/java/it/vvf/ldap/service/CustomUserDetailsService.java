package it.vvf.ldap.service;

import java.util.Collections;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;

import it.vvf.ldap.util.LDAPConstants;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  

	private static final Logger log = LogManager.getLogger(CustomUserDetailsService.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(LDAPConstants.OBJECT_CLASS, LDAPConstants.PERSON)).and(new EqualsFilter(LDAPConstants.USER_PRINCIPAL_NAME, username));

        List<LdapUserDetails> users = ldapTemplate.search("", filter.encode(), new LdapUserDetailsMapper());

        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        LdapUserDetails ldapUser = users.get(0);
        // Return the user details retrieved from LDAP
        return new User(ldapUser.getUsername(), ldapUser.getPassword(), ldapUser.getAuthorities());
    }

    private class LdapUserDetailsMapper implements AttributesMapper<LdapUserDetails> {
        public static final String DISTINGUISHED_NAME = "distinguishedName";

		@Override
        public LdapUserDetails mapFromAttributes(Attributes attributes) throws NamingException {
            LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();

            try {
                essence.setUsername((String) attributes.get(LDAPConstants.USER_PRINCIPAL_NAME).get());
                essence.setDn((String) attributes.get(DISTINGUISHED_NAME).get());
                // Password is handled separately, so we can leave it as an empty string
                essence.setPassword(""); // LDAP password is not typically stored in the user details
            } catch (Exception e) {
                log.error("Error mapping LDAP attributes", e);
            }

            essence.setAuthorities(Collections.emptyList()); // Set authorities if necessary
            return essence.createUserDetails();
        }
    }
}

