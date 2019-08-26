package inteligenca;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Tuple;

public class Move {
	int numPlayoffs; /* Number of playoffs which started from this move. */
	int numWins; /* Number of won playoffs (with added children's playoffs). */
	Polje side; /* Side for which we are calculating. */
	Igra position; /* Position after this move, starting point of calculation. */
	Move parent; /* Previous move in tree. */
	List<Move> children; /* Next moves in tree. */
	boolean lastResult; /* Holds result of last playoff, true if win for side. */
	boolean isFinal; /* true if this move ends the game. */
	
	Move(Polje side, Igra position) {
		/* This initializer is used for initial candidates. */
		this.side = side;
		this.position = position;
		numPlayoffs = 0;
		numWins = 0;
		parent = null;
		children = new LinkedList<Move>();
	}
	
	Move(Polje side, Igra position, Move parent) {
		/* This initializer is used for deeper candidates. */
		this.side = side;
		this.position = position;
		numPlayoffs = 0;
		numWins = 0;
		this.parent = parent;
		children = new LinkedList<Move>();
	}
	
	boolean runPlayoff() {
		/* Run a playoff. Returns true if side wins, returns false otherwise. */
		Igra simulator = new Igra(position);
		
		
		if (!isFinal) {
			while (!(simulator.obstajaPot(Igralec.MODRI) || simulator.obstajaPot(Igralec.RDECI))) {
				
				Tuple randMov = simulator.randMov();
				
				simulator.poteza(randMov.getX(), randMov.getY());
				
			}
		}
		
		
		boolean result = (simulator.obstajaPot((side == Polje.MODRO) ? Igralec.MODRI : Igralec.RDECI));
		
		return result;
	}
	
	void backPropagate() {
		/* Updates own weight and backpropagates last result. */
		numWins++;
		if (parent != null) {
			parent.backPropagate();
		}
		
	}
	
	List<Move> deepestChildren() {
		/* Returns list of deepest children. */
		if (children.isEmpty()) {
			List<Move> lst = new LinkedList<Move>();
			lst.add(this);
			return lst;
		}
		
		List<Move> lst = new LinkedList<Move>();
		
		for (Move child : children) {
			lst.addAll(child.deepestChildren());
		}
		
		return lst;
	}
	
	void genChildren() {
		/* Generates children. */
		List<Tuple> coords = position.allMov();

		for (Tuple coord : coords) {
			Igra copyPos = new Igra(position);
			copyPos.poteza(coord.getX(), coord.getY());
			children.add(new Move(side, copyPos, this));
		}
	}
}

