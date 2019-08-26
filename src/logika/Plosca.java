package logika;

import java.util.LinkedList;
import java.util.List;

public class Plosca {
    public static int velikost = 5;
    public Polje[][] plosca;

    // Konstruktor, ki naredi novo ploščo
    Plosca() {
        plosca = new Polje[velikost][velikost];
    }

    // Nastavi začetno ploščo s praznimi polji
    void init() {
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                plosca[i][j] = Polje.PRAZNO;
            }
        }
    }

    // Pridobimo vse sosede posameznega polja
    List<Tuple> pridobiSosede(int x, int y) {
        List<Tuple> pomozni = new LinkedList<>();
        if (y + 1 < velikost) pomozni.add(new Tuple(x, y + 1));
        if (y - 1 >= 0) pomozni.add(new Tuple(x, y - 1));
        if (x - 1 >= 0 && y + 1 < velikost) pomozni.add(new Tuple(x - 1, y + 1));
        if (x - 1 >= 0) pomozni.add(new Tuple(x - 1, y));
        if (x + 1 < velikost) pomozni.add(new Tuple(x + 1, y));
        if (x + 1 < velikost && y - 1 >= 0) pomozni.add(new Tuple(x + 1, y - 1));
        return pomozni;
    }

    // Vrne prednost (x, y) polja
    Polje pridobiPolje(int x, int y) {
        return plosca[x][y];
    }

    // Postavi ustrezno barvo na (x, y) polje
    boolean postavi(Igralec igralec, int x, int y) {
        if (this.plosca[x][y] == Polje.PRAZNO) {
            this.plosca[x][y] = (igralec == Igralec.MODRI ? Polje.MODRO : Polje.RDECE);
            return true;
        }
        return false;
    }
}
