package inteligenca;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Tuple;

import javax.swing.*;
import java.util.List;
import java.util.Random;

public class Nakljucno extends SwingWorker<Tuple, Object> {

    private GlavnoOkno okno;

    public Nakljucno(GlavnoOkno okno) {
        this.okno = okno;
    }

    @Override
    protected Tuple doInBackground() throws Exception {
        Igra igra = okno.kopijaIgre();
        Thread.sleep(100);
        Random r = new Random();
        List<Tuple> poteze = igra.moznePoteze();
        return poteze.get(r.nextInt(poteze.size()));
    }

    @Override
    protected void done() {
        try {
            Tuple poteza = this.get();
            if (poteza != null) okno.odigraj(poteza);
        } catch (Exception ignored) {}
    }
}
