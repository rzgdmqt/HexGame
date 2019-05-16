package logika;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Igra {
    public Plosca plosca;
    public Igralec kdo;
    private Tuple zadnjaPoteza;
    private int steviloPotez;

    // Konstruktor za igro
    public Igra(Igralec prvi) {
        this.plosca = new Plosca();
        this.plosca.init();
        this.kdo = prvi;
        this.steviloPotez = 0;
    }

    // Konstruktor za kopijo trenutne igre
    public Igra(Igra igra) {
        this.plosca = new Plosca();
        for (int i = 0; i < Plosca.velikost; i++) {
            for (int j = 0; j < Plosca.velikost; j++) {
                this.plosca.plosca[j][i] = igra.plosca.plosca[j][i];
            }
        }
        this.zadnjaPoteza = igra.zadnjaPoteza;
        this.kdo = igra.kdo;
        this.steviloPotez = igra.steviloPotez;
    }

    /*
    Vrne true če za podanega igralca pot iz začetne točke obstaja, sicer vrne false. (BFS)
     */
    private boolean obstajaPot(Igralec igralec, Tuple zacetek) {
        HashSet<Tuple> videni = new HashSet<>();
        List<Tuple> queue = new LinkedList<>();
        boolean zacetekM = false;
        boolean zacetekR = false;
        boolean konecM = false;
        boolean konecR = false;
        queue.add(zacetek);
        videni.add(zacetek);
        if (plosca.pridobiPolje(zacetek.getX(), zacetek.getY()) ==
                (igralec == Igralec.MODRI ? Polje.MODRO : Polje.RDECE)) {
            while (!queue.isEmpty()) {
                Tuple trenutna = queue.get(0);
                queue.remove(0);
                for (Tuple tocka : plosca.pridobiSosede(trenutna.getX(), trenutna.getY())) {
                    if (!videni.contains(tocka) &&
                            plosca.pridobiPolje(tocka.getX(), tocka.getY()) ==
                                    (igralec == Igralec.MODRI ? Polje.MODRO : Polje.RDECE)) {
                        queue.add(tocka);
                        videni.add(tocka);
                    }
                }
            }
            for (Tuple tocka : videni) {
                if (igralec == Igralec.RDECI && tocka.getY() == Plosca.velikost - 1) konecR = true;
                if (igralec == Igralec.RDECI && tocka.getY() == 0) zacetekR = true;
                if (igralec == Igralec.MODRI && tocka.getX() == Plosca.velikost - 1) konecM = true;
                if (igralec == Igralec.MODRI && tocka.getX() == 0) zacetekM = true;
            }
        }
        return (zacetekM && konecM || zacetekR && konecR);
    }

    // Zmaga, Poraz, Ali kdo je na potezi
    public Stanje stanje() {
        if (obstajaPot(kdo == Igralec.MODRI ?
                Igralec.RDECI : Igralec.MODRI, (zadnjaPoteza == null ? new Tuple(0, 0) : zadnjaPoteza)))
            return kdo == Igralec.MODRI ? Stanje.Z_RDECI : Stanje.Z_MODRI;
        return kdo == Igralec.MODRI ? Stanje.NP_MODRI : Stanje.NP_RDECI;
    }

    // naredimo potezo na (x, y) polje
    public boolean poteza(int x, int y) {
        if (plosca.postavi(kdo, x, y)) {
            this.zadnjaPoteza = new Tuple(x, y);
            kdo = kdo.nasprotnik();
            steviloPotez++;
//            for (Polje[] p : plosca.plosca) {  // za debuganje
//                System.out.println(Arrays.toString(p));
//            }
            return true;
        }
        return false;
    }

    // Pridobimo seznam možnih potez
    public List<Tuple> moznePoteze() {
        List<Tuple> moznosti = new LinkedList<>();
        for (int i = 0; i < Plosca.velikost; i++) {
            for (int j = 0; j < Plosca.velikost; j++) {
                if (plosca.pridobiPolje(i, j) == Polje.PRAZNO) moznosti.add(new Tuple(i, j));
            }
        }
        return moznosti;
    }
}
