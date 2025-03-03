package it.vvf.ldap.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationCodeService {

    private String currentCode;
    private LocalDateTime expirationTime;

    public String generateConfirmationCode() {
        currentCode = UUID.randomUUID().toString();
        expirationTime = LocalDateTime.now().plusMinutes(5); // Il codice è valido per 5 minuti
        return currentCode;
    }

    public boolean isValidCode(String code) {
        return code.equals(currentCode) && LocalDateTime.now().isBefore(expirationTime);
    }
}