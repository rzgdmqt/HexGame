package logika;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Igra {
    public Plosca plosca;
    private Igralec kdo;
    private int steviloPotez;

    public Igra(Igralec prvi) {
        this.plosca = new Plosca();
        this.plosca.init();
        this.kdo = prvi;
        this.steviloPotez = 0;
    }

    public Igra(Igra igra) {
        this.plosca = new Plosca();
        for (int i = 0; i < Plosca.velikost; i++) {
            for (int j = 0; j < Plosca.velikost; j++) {
                this.plosca.plosca[j][i] = igra.plosca.plosca[j][i];
            }
        }
        this.kdo = igra.kdo;
        this.steviloPotez = igra.steviloPotez;
    }
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // TODO Na tem delu je treba popraviti victory chech za modrega
    /*
    Vrne true če za podanega igralca pot iz začetne točke obstaja, sicer vrne false. (BFS)
     */
    private boolean obstajaPot(Igralec igralec, Tuple zacetek) {
        HashSet<Tuple> videni = new HashSet<>();
        List<Tuple> queue = new LinkedList<>();
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
                if (igralec == Igralec.MODRI && tocka.getX() == Plosca.velikost - 1) return true;
                if (igralec == Igralec.RDECI && tocka.getY() == Plosca.velikost - 1) return true;
            }
        }
        return false;
    }

    public Stanje stanje() {
        for (int i = 0; i < Plosca.velikost; i++) {
            if (obstajaPot(kdo == Igralec.MODRI ?
                            Igralec.RDECI : Igralec.MODRI,
                    kdo == Igralec.MODRI ?
                            new Tuple(i, 0) : new Tuple(0, i))
            ) return kdo == Igralec.MODRI ? Stanje.Z_RDECI : Stanje.Z_MODRI;
        }
        if (steviloPotez == Math.pow(Plosca.velikost, 2)) {
            return Stanje.NEODLOCENO;
        }
        return kdo == Igralec.MODRI ? Stanje.NP_MODRI : Stanje.NP_RDECI;
    }
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public boolean poteza(int x, int y) {
        if (plosca.postavi(kdo, x, y)) {
            kdo = kdo.nasprotnik();
            steviloPotez++;
            return true;
        }
        return false;
    }

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
