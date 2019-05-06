package inteligenca;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Tuple;

import javax.swing.*;
import java.util.List;

public class Pametno extends SwingWorker<Tuple, Object> {

    private GlavnoOkno okno;

    public Pametno(GlavnoOkno okno) {
        this.okno = okno;
    }

    @Override
    protected Tuple doInBackground() throws Exception {
        Igra igra = okno.kopijaIgre();
        Thread.sleep(100);
//            Random r = new Random();
//            List<Tuple> poteze = igra.moznePoteze();
//            return poteze.get(r.nextInt(poteze.size()));
        List<OcenjenaPoteza> ocenjenePoteze = Minimax.oceniPoteze(igra, 3, igra.kdo.nasprotnik());
        return Minimax.maxPoteza(ocenjenePoteze);
    }

    @Override
    protected void done() {
        try {
            Tuple poteza = this.get();
            if (poteza != null) okno.odigraj(poteza);
        } catch (Exception ignored) {
        }
    }
}