package it.vvf.ldap.util;

import java.util.Arrays;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sezioni disponibili")
public enum Section {
	@Schema(description = "Sezione comune")
    COMMON,
    @Schema(description = "Staff Comandante")
    STAFF_COMANDANTE,
    @Schema(description = "Vicario")
    VICARIO,
    @Schema(description = "Segreteria Comandante")
    SEGRETERIA_COMANDANTE,
    @Schema(description = "Uffcio Sanitario")
    UFFICIO_SANITARIO,
    @Schema(description = "Comunicazione Istituzionale")
    COMUNICAZIONE_ISTITUZIONALE,
    @Schema(description = "Sub Consegnatario")
    SUB_CONSEGNATARIO,
    @Schema(description = "Logistico Gestionale")
    LOGISTICO_GESTIONALE,
    @Schema(description = "Amministrazione Generale")
    AMMINISTRAZIONE_GENERALE,
    @Schema(description = "Risorse Umane")
    RISORSE_UMANE,
    @Schema(description = "Mobilità Personale")
    MOBILITA_PERSONALE,
    @Schema(description = "Risorse Finanziarie")
    RISORSE_FINANZIARIE,
    @Schema(description = "Acquisti")
    ACQUISTI,
    @Schema(description = "Prevenzione e Protezione")
    PREVENZIONE_PROTEZIONE,
    @Schema(description = "Formazione")
    FORMAZIONE,
    @Schema(description = "Formazione Professionale")
    FORMAZIONE_PROFESSIONALE,
    @Schema(description = "Forzazione Esterna")
    FORMAZIONE_ESTERNA,
    @Schema(description = "Formazione Volontari")
    FORMAZIONE_VOLONTARI,
    @Schema(description = "Prevenzione Incendi")
    PREVENZIONE_INCENDI,
    @Schema(description = "Polizia Giudiziaria")
    POLIZIA_GIUDIZIARIA,
    @Schema(description = "Vigilanza Antincendi")
    VIGILANZA_ANTINCENDI,
    @Schema(description = "Emergenza Soccorso")
    EMERGENZA_SOCCORSO,
    @Schema(description = "Pianificazione Operativa")
    PIANIFICAZIONE_OPERATIVA,
    @Schema(description = "Cordinamento Sala Operativa")
    COORDINAMENTO_SALA_OPERATIVA,
    @Schema(description = "Servizio Aeroportuale")
    SERVIZIO_AEROPORTUALE,
    @Schema(description = "SMZT e SA")
    SMZT_SA,
    @Schema(description = "TAS, SAF")
    TAS_SAF,
    @Schema(description = "NBCR")
    NBCR,
    @Schema(description = "Tecniche Innovative")
    TECNICHE_INNOVATIVE,
    @Schema(description = "AIB")
    AIB,
    @Schema(description = "USAR, STCS, GOS")
    USAR_STCS_GOS,
    @Schema(description = "TPSS")
    TPSS,
    @Schema(description = "Risorse Logistiche")
    RISORSE_LOGISTICHE,
    @Schema(description = "Sedi di Servizio")
    SEDI_SERVIZIO,
    @Schema(description = "Laboratori Tecnici")
    LABORATORI_TECNICI,
    @Schema(description = "Magazzini Tecnici")
    MAGAZZINI_TECNICI,
    @Schema(description = "Autorimessa")
    AUTORIMESSA,
    @Schema(description = "Officina")
    OFFICINA,
    @Schema(description = "ICT e Telecomunicazioni")
    ICT_TELECOMUNICAZIONI;

    /**
     * Verifica se una sezione è valida.
     *
     * @param section La sezione da verificare
     * @return true se la sezione è valida, false altrimenti
     */
    public static boolean isValidSection(Section section) {
        return section != null;
    }

    /**
     * Verifica se una stringa rappresenta una sezione valida.
     *
     * @param sectionName Il nome della sezione da verificare
     * @return true se la stringa rappresenta una sezione valida, false altrimenti
     */
    public static boolean isValidSection(String sectionName) {
        try {
            Section.valueOf(sectionName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Converte una stringa in una Section.
     *
     * @param sectionName Il nome della sezione da convertire
     * @return La Section corrispondente
     * @throws IllegalArgumentException se la stringa non corrisponde a una Section valida
     */
    public static Section fromString(String sectionName) {
        try {
            return Section.valueOf(sectionName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Sezione non valida: " + sectionName);
        }
    }

    /**
     * Restituisce un array di tutte le sezioni valide.
     *
     * @return Un array di tutte le sezioni valide
     */
    public static Section[] getAllSections() {
        return Section.values();
    }

    /**
     * Restituisce una stringa con tutte le sezioni valide, separate da virgole.
     *
     * @return Una stringa con tutte le sezioni valide
     */
    public static String getAllSectionsAsString() {
        return Arrays.toString(Section.values());
    }
}