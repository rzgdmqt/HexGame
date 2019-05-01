package logika;

import java.util.LinkedList;
import java.util.List;

public class Plosca {
    public static int velikost = 7;
    public Polje[][] plosca;


    Plosca() {
        plosca = new Polje[velikost][velikost];
    }

    void init() {
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                plosca[i][j] = Polje.PRAZNO;
            }
        }
    }

    List<Tuple> pridobiSosede(int x, int y) {
        List<Tuple> pomozni = new LinkedList<>();
        if (y + 1 < velikost) pomozni.add(new Tuple(x, y + 1));
        if (y - 1 >= 0) pomozni.add(new Tuple(x, y - 1));
        if (x - 1 >= 0 && y + 1 < velikost) pomozni.add(new Tuple(x - 1, y + 1));
        if (x - 1 >= 0) pomozni.add(new Tuple(x - 1, y));
        if (x + 1 < velikost) pomozni.add(new Tuple(x + 1, y));
        if (x + 1 < velikost && y - 1 > 0) pomozni.add(new Tuple(x + 1, y - 1));
        return pomozni;
    }

    Polje pridobiPolje(int x, int y) {
        return plosca[x][y];
    }

    boolean postavi(Igralec igralec, int x, int y) {
        if (this.plosca[x][y] == Polje.PRAZNO) {
            this.plosca[x][y] = (igralec == Igralec.MODRI ? Polje.MODRO : Polje.RDECE);
            return true;
        }
        return false;
    }
}
