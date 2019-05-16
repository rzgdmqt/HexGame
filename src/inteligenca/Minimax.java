package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import logika.Tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Minimax {

    private static final Random RANDOM = new Random();
    private static final int ZMAGA = (1 << 30);
    private static final int PORAZ = -ZMAGA;

    public static List<OcenjenaPoteza> oceniPoteze(Igra igra, int globina, Igralec jaz) {
        List<OcenjenaPoteza> ocenjenePoteze = new LinkedList<>();
        List<Tuple> moznePoteze = igra.moznePoteze();
        for (Tuple t: moznePoteze) {
            igra.poteza(t.getX(), t.getY());
            int ocena = minimaxPozicijo(igra, globina - 1, jaz);
            ocenjenePoteze.add(new OcenjenaPoteza(t, ocena));
        }
        return ocenjenePoteze;
    }

    public static int minimaxPozicijo(Igra igra, int globina, Igralec jaz) {
        Stanje stanje = igra.stanje();
        if (stanje == Stanje.Z_MODRI) return (jaz == Igralec.MODRI ? ZMAGA : PORAZ);
        else if (stanje == Stanje.Z_RDECI) return (jaz == Igralec.RDECI ? ZMAGA : PORAZ);
        if (globina == 0) return oceniPozicijo(igra, jaz);
        List<OcenjenaPoteza> ocenjenePoteze = oceniPoteze(igra, globina, jaz);
        if (igra.kdo == jaz) return maxOcena(ocenjenePoteze);
        else return minOcena(ocenjenePoteze);
    }

    private static int maxOcena(List<OcenjenaPoteza> ocenjenePoteze) {
        int max = PORAZ;
        for (OcenjenaPoteza p : ocenjenePoteze) {
            if (p.vrednost > max) max = p.vrednost;
        }
        return max;
    }

    public static Tuple maxPoteza(List<OcenjenaPoteza> ocenjenePoteze) {
        int max = PORAZ;
        Tuple poteza = null;
        for (OcenjenaPoteza p : ocenjenePoteze) {
            if (p.vrednost >= max) {
                max = p.vrednost;
                poteza = p.poteza;
            }
        }
        return poteza;
    }

    private static int minOcena(List<OcenjenaPoteza> ocenjenePoteze) {
        int min = ZMAGA;
        for (OcenjenaPoteza p : ocenjenePoteze) {
            if (p.vrednost < min) min = p.vrednost;
        }
        return min;
    }

//    TODO ocena poteze, trenutno deluje random
    private static int oceniPozicijo(Igra igra, Igralec jaz) {
        return RANDOM.nextInt(201) - 100;
    }

}
