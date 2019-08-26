package inteligenca;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Plosca;
import logika.Polje;
import logika.Tuple;

import javax.swing.*;

public class MonteCarlo extends SwingWorker<Tuple, Object> {
	/* Runs Monte Carlo Tree Search, as implemented in MoveTree & Move helper classes.
	 * Algorithm reference: https://en.wikipedia.org/wiki/Monte_Carlo_tree_search
	 * This version is a bit simplified. */

    private GlavnoOkno okno;
    
    /* Search parameters.
     * In case game runs too slowly for your taste lower them.
     * In case game is too easy, raise them. */
    private int numDepth; /* Depth to which moves are evaluated. */
    private int numPlayoffs; /* Number of random playoffs used to evaluate candidates. */
    
    public MonteCarlo(GlavnoOkno okno) {
        this.okno = okno;
        numDepth = 9;
        numPlayoffs = 7;
    }

    @Override
    protected Tuple doInBackground() throws Exception {
        Igra igra = okno.kopijaIgre();
        if (igra.zadnjaPoteza != null) {
        	
        }
        MoveTree tree = new MoveTree(igra, igra.kdo == Igralec.MODRI ? Polje.MODRO : Polje.RDECE);
        
        for (int depth = 0; depth <= 9; depth++) {
        	tree.playRound(7);
        }
        
        if (igra.steviloPotez > (Plosca.velikost * Plosca.velikost) / 2) {
        	/* Due to space filling nature of game,
        	 * MCTS runs faster and faster.
        	 * In order to not have moves unnervingly fast,
        	 * we slow it down a tad after game halfpoint. */
        	Thread.sleep(500);
        }
        
        Tuple best = tree.getBest();
        
        return best;
    }

    @Override
    protected void done() {
        try {
            Tuple poteza = this.get();
            if (poteza != null) okno.odigraj(poteza);
        } catch (Exception ignored) {}
    }
}
