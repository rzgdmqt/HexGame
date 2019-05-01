package gui;

import logika.Igralec;
import logika.Tuple;

public class Clovek extends Strateg{
    private GlavnoOkno okno;

    Clovek(GlavnoOkno okno, Igralec jaz) {
        this.okno = okno;

    }
    @Override
    public void naPotezi() {

    }

    @Override
    public void prekini() {

    }

    @Override
    public void klik(int i, int j) {
        okno.odigraj(new Tuple(i, j));

    }
}
