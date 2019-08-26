package inteligenca;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import logika.Igra;
import logika.Polje;
import logika.Tuple;

public class MoveTree {
	/* Implements move tree with methods needed for MTCS. */
	
	Igra startPos; /* Game position from which calculation begins. */
	Polje side; /* Side for which we are choosing a move. */
	List<Move> initMoves; /* All legal moves in game position. */
	boolean isFirstRound; /* Indicates if this is first round of playoffs - if so, some init is needed. */
	
	MoveTree(Igra pos, Polje side) {
		startPos = pos;
		this.side = side;
		List<Tuple> coords = pos.allMov();
		initMoves = new LinkedList<Move>();
		isFirstRound = true;
		
		/* Initialize candidates. */
		for (Tuple coord : coords) {
			Igra copyPos = new Igra(startPos);
			copyPos.poteza(coord.getX(), coord.getY());
			
			initMoves.add(new Move(side, copyPos));
		}
	}
	
	void playRound(int maxPlayoffs) {
		/* Play a round of maxPlayoffs per move at current depth. */
		
		List<Move> candidates = null;
		
		if (isFirstRound) {
			/* If first round we playoff candidates from startPos. */
			candidates = initMoves;
			isFirstRound = false;
		} else {
			/* Else our candidates are deepest children nodes. */
			candidates = new LinkedList<Move>();
			for (Move mov : initMoves) {
				candidates.addAll(mov.deepestChildren());
			}
		}
		
		for (int i = 0; i <= maxPlayoffs; i++) {
			for (Move candidate : candidates) {
				if (candidate.runPlayoff()) {
					candidate.backPropagate();
				}
			}
		}
		
		candidates.sort(new sortByWeight());
		
		/* We will look at 3 most promising moves. */
		if(candidates.size() > 3) {
			candidates = candidates.subList(0, 3);
		}
		
		for (Move candidate : candidates) {
			candidate.genChildren();
		}
	}
	
	Tuple getBest() {
		/* Returns move which was calculated to lead to most wins in random playoffs (with children playoff results added). */
		initMoves.sort(new sortByWeight());
		Move bestMov = initMoves.get(0);
		
		return bestMov.position.zadnjaPoteza;
	}
}


class sortByWeight implements Comparator<Move> {
	/* Compares move nodes in tree by number of wins. */
	public int compare(Move a, Move b) {
		return b.numWins - a.numWins;
	}
}