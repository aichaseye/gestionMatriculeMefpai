package sn.seye.gesmat.mefpai.domain.enumeration;

/**
 * The Etat enumeration.
 */
public enum Etat {
    REDOUBLANT("E"),
    PASSANT("E");

    private final String value;

    Etat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
