package it.vvf.ldap.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.expiration}")
	private long expirationTime;

	private SecretKey signingKey;
	private SecretKey encryptionKeySpec;

	@PostConstruct
	public void init() {
		
		// Inizializza la chiave di firma (HMAC-SHA256)
		this.signingKey = Jwts.SIG.HS256.key().build();
		
		// Inizializza la chiave di cifratura (AES-256-GCM)
		byte[] keyBytes = new byte[32]; // 256 bit
		this.encryptionKeySpec = new SecretKeySpec(keyBytes, "AES"); // Crea una chiave AES-256
	}

	// Metodo per generare il token firmato e cifrato
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userDetails.getUsername());
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		try {
			String signedToken = Jwts.builder().claims(claims).subject(subject).issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis() + expirationTime)).signWith(signingKey).compact();

			return Jwts.builder().content(signedToken).encryptWith(encryptionKeySpec, Jwts.ENC.A256GCM).compact();
		} catch (Exception e) {
			throw new RuntimeException("Errore nella generazione del token JWT: " + e.getMessage(), e);
		}
	}

}
