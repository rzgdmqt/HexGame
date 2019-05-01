package logika;

public enum  Igralec {
    MODRI, RDECI;

    public Igralec nasprotnik() {
        if (this == MODRI) return RDECI;
        else return MODRI;
    }
}
