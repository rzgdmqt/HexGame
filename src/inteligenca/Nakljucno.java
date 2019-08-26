package inteligenca;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Tuple;

import javax.swing.*;

public class Nakljucno extends SwingWorker<Tuple, Object> {

    private GlavnoOkno okno;

    public Nakljucno(GlavnoOkno okno) {
        this.okno = okno;
    }

    @Override
    protected Tuple doInBackground() throws Exception {
        Igra igra = okno.kopijaIgre();
        Thread.sleep(100);
        return (igra.randMov());
    }

    @Override
    protected void done() {
        try {
            Tuple poteza = this.get();
            if (poteza != null) okno.odigraj(poteza);
        } catch (Exception ignored) {}
    }
}
