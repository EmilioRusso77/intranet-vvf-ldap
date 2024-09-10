package it.vvf.ldap.util;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ruoli utente disponibili")
public enum UserRole {
    @Schema(description = "Utente standard")
    USER,
    @Schema(description = "Amministratore")
    ADMIN,
    @Schema(description = "Responsabile")
    RESPONSABILE
}