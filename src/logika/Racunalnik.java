package logika;

import gui.GlavnoOkno;
import inteligenca.Nakljucno;
import inteligenca.Pametno;

import javax.swing.*;

public class Racunalnik extends Strateg {
    private GlavnoOkno okno;
    private SwingWorker<Tuple, Object> mislec;

    public Racunalnik(GlavnoOkno okno, Igralec jaz) {
        this.okno = okno;
    }

    @Override
    public void naPotezi() {
//        če želimo minimax poteze:
//        mislec = new Pametno(okno);
//        če želimo nakjučne poteze:
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
