package it.vvf.ldap.util;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import it.vvf.ldap.dto.UserRoleDto;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.expiration}")
	private long expirationTime;

	@Value("${jwt.signingKeyBase64}")
	private String signingKeyBase64;

	
	@Value("${jwt.encryptionKeyBase64}")
	private String encryptionKeyBase64;


	private SecretKey signingKey;
	private SecretKey encryptionKeySpec;

	@PostConstruct
	public void init() {
		/*
		// Generazione chiave di firma (HMAC-SHA256) salvate nell'application.properties
		Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String signingKeyBase64 = Base64.getEncoder().encodeToString(signingKey.getEncoded());
		System.err.println(signingKeyBase64);
		// Generazione chiave di cifratura (AES-256-GCM) salvate nell'application.properties
		byte[] encryptionKeyBytes = new byte[32]; // 32 byte = 256 bit
		new SecureRandom().nextBytes(encryptionKeyBytes);
		String encryptionKeyBase64 = Base64.getEncoder().encodeToString(encryptionKeyBytes);
		System.err.println(encryptionKeyBase64);
		*/
		
	    // Inizializza la chiave di firma (HMAC-SHA256)
	    byte[] signingKeyBytes = Base64.getDecoder().decode(signingKeyBase64);
	    if (signingKeyBytes.length < 32) { // 32 byte = 256 bit
	        throw new IllegalArgumentException("Signing key is too short for HMAC-SHA256. It should be at least 256 bits.");
	    }
	    this.signingKey = new SecretKeySpec(signingKeyBytes, "HmacSHA256");

	    // Inizializza la chiave di cifratura (AES-256-GCM)
	    byte[] encryptionKeyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
	    if (encryptionKeyBytes.length != 32) { // 32 byte = 256 bit
	        throw new IllegalArgumentException("Encryption key must be 256 bits (32 bytes) long for AES-256-GCM.");
	    }
	    this.encryptionKeySpec = new SecretKeySpec(encryptionKeyBytes, "AES");
	}

	// Metodo per generare il token firmato e cifrato
	public String generateToken(UserRoleDto userRoleDto) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userRoleDto.getUsername());
		claims.put("role", userRoleDto.getRole());
		claims.put("sections", userRoleDto.getSections());
		return createToken(claims, userRoleDto.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		try {
			String signedToken = Jwts.builder().claims(claims).subject(subject).issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(signingKey).compact();
           //return signedToken;	
			return Jwts.builder().content(signedToken).encryptWith(encryptionKeySpec, Jwts.ENC.A256GCM).compact();
		} catch (Exception e) {
			throw new RuntimeException("Errore nella generazione del token JWT: " + e.getMessage(), e);
		}
	}

}
