package logika;

public enum Igralec {
    MODRI, RDECI;

    // Zamenja igralca
    public Igralec nasprotnik() {
    	
    	return ((this == MODRI) ? RDECI : MODRI);
    }
}
