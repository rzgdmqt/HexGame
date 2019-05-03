package logika;

public enum Igralec {
    MODRI, RDECI;

    // Zamenja igralca
    public Igralec nasprotnik() {
        if (this == MODRI) return RDECI;
        else return MODRI;
    }
}
