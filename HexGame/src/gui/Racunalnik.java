package gui;

import inteligenca.Nakljucno;
import logika.Igralec;
import logika.Tuple;

import javax.swing.*;

public class Racunalnik extends Strateg {
    private GlavnoOkno okno;
    private SwingWorker<Tuple, Object> mislec;

    Racunalnik(GlavnoOkno okno, Igralec jaz) {
        this.okno = okno;

    }

    @Override
    public void naPotezi() {
        mislec = new Nakljucno(okno);
        mislec.execute();
    }

    @Override
    public void prekini() {
        if (mislec != null) {
            mislec.cancel(true);
        }

    }

    @Override
    public void klik(int i, int j) {

    }
}
