package logika;

import java.util.List;

public class Igra {
    public Plosca plosca;
    public Igralec kdo;
    public Tuple zadnjaPoteza;
    public int steviloPotez;
    private MoveGenerator movGen; /* Move generator. */
    private EqvClass eqvRdeci; /* Eqv classes for rdeci player. */
    private EqvClass eqvModri; /* Eqv classes for modri player. */
    private boolean original; /* For easier debug. */

    // Konstruktor za igro
    public Igra(Igralec prvi) {
        this.plosca = new Plosca();
        this.plosca.init();
        this.kdo = prvi;
        this.steviloPotez = 0;
        this.movGen = new MoveGenerator();
        this.eqvRdeci = new EqvClass();
        this.eqvModri = new EqvClass();
        original = true;
    }

    // Konstruktor za kopijo trenutne igre
    public Igra(Igra igra) {
        this.plosca = new Plosca();
        for (int i = 0; i < Plosca.velikost; i++) {
            for (int j = 0; j < Plosca.velikost; j++) {
                this.plosca.plosca[j][i] = igra.plosca.plosca[j][i];
            }
        }
        this.zadnjaPoteza = igra.zadnjaPoteza;
        this.kdo = igra.kdo;
        this.steviloPotez = igra.steviloPotez;
        this.movGen = new MoveGenerator(igra.movGen);
        this.eqvRdeci = new EqvClass(igra.eqvRdeci);
        this.eqvModri = new EqvClass(igra.eqvModri);
        original = false;
    }

    /* Tests if path connects igralec's edges.
     * (Implemented through equivalence classes) */
    public boolean obstajaPot(Igralec igralec) {
    	EqvClass eqvC = (igralec == Igralec.MODRI) ? eqvModri : eqvRdeci;
    	return eqvC.isWon((igralec == Igralec.MODRI) ? Polje.MODRO : Polje.RDECE, plosca);
    }

    // Zmaga, Poraz, Ali kdo je na potezi
    public Stanje stanje() {   	
        if (obstajaPot(kdo == Igralec.MODRI ?
                Igralec.RDECI : Igralec.MODRI))
            return kdo == Igralec.MODRI ? Stanje.Z_RDECI : Stanje.Z_MODRI;
        return kdo == Igralec.MODRI ? Stanje.NP_MODRI : Stanje.NP_RDECI;
    }

    // naredimo potezo na (x, y) polje
    public boolean poteza(int x, int y) {
        if (plosca.postavi(kdo, x, y)) {
            this.zadnjaPoteza = new Tuple(x, y);
     
            /* Insert move into Equivalence Class. */
            EqvClass eqvC = (kdo == Igralec.MODRI) ? eqvModri : eqvRdeci;
            eqvC.insert(new Tuple(x, y), plosca);
            
            /* Remove played move from available moves in MoveGenerator. */
            movGen.remove(new Tuple(x, y));
            
            this.kdo = kdo.nasprotnik();
            
            steviloPotez++;
            return true;
        }
        return false;
    }
    
    public List<Tuple> allMov() {
    	/* Returns all legal moves. */
    	return movGen.getAll();
    }

    public Tuple randMov() {
    	/* Returns random legal move. */
    	Tuple mov = movGen.getRandom();
    	return mov;
    }
}
