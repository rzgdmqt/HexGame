package logika;

import java.util.ArrayList;
import java.util.List;

public class EqvClass {
	/* Used for determining winning condition.
	 * 
	 * We basically group same-coloured cells into equivalence classed,
	 * based on relation: "is-connected?".
	 * 
	 * When move is inserted we just need to consider it's neighbours,
	 * since it can only connect them.
	 * 
	 * To check victory condition we just compare eqv class tags
	 * of appropriate edge cells.
	 * 
	 * Original implementation based on emulating pointers had potential
	 * to be faster, but proved too hard to debug for not enough benefit. */

	private int[][] cellToTag; /* Maps tuples of coordinates to tags, which name eqv classes. */
	private List<ArrayList<Tuple>> tagToCell; /* Maps eqv class to cells, which belong to it. */
	private int nextTag; /* Free tag for naming new eqv classes. */
	
	/* Edges of the board, for checking win condition. */
	private static Tuple[] rob_modri_a;
	private static Tuple[] rob_modri_b;
	private static Tuple[] rob_rdeci_a;
	private static Tuple[] rob_rdeci_b;
	
	EqvClass() {
		rob_modri_a = new Tuple[Plosca.velikost];
		rob_modri_b = new Tuple[Plosca.velikost];
		rob_rdeci_a = new Tuple[Plosca.velikost];
		rob_rdeci_b = new Tuple[Plosca.velikost];
		cellToTag = new int[Plosca.velikost][Plosca.velikost];
		tagToCell = new ArrayList<ArrayList<Tuple>>();
		nextTag = 0;
		
		for (int x = 0; x < Plosca.velikost; x++) {
			for (int y = 0; y < Plosca.velikost; y++) {
				cellToTag[x][y] = -1; /* -1 signifies that cell belongs to no eqv class. */
			}
		}
		
		for (int i = 0; i < Plosca.velikost; i++) {
			rob_rdeci_a[i] = new Tuple(i, 0);
			rob_rdeci_b[i] = new Tuple(i, Plosca.velikost - 1);
			rob_modri_a[i] = new Tuple(0, i);
			rob_modri_b[i] = new Tuple(Plosca.velikost - 1, i);
		}
	}
	
	EqvClass(EqvClass toBeCopied) {
		cellToTag = new int[Plosca.velikost][Plosca.velikost];
		tagToCell = new ArrayList<ArrayList<Tuple>>();
		nextTag = toBeCopied.nextTag;
		
		for (int x = 0; x < Plosca.velikost; x++) {
			for (int y = 0; y < Plosca.velikost; y++) {
				cellToTag[x][y] = toBeCopied.cellToTag[x][y];
			}
		}
		
		for (ArrayList<Tuple> cells : toBeCopied.tagToCell) {
			tagToCell.add(cells);
		}
		
		for (int i = 0; i < Plosca.velikost; i++) {
			rob_rdeci_a[i] = new Tuple(i, 0);
			rob_rdeci_b[i] = new Tuple(i, Plosca.velikost - 1);
			rob_modri_a[i] = new Tuple(0, i);
			rob_modri_b[i] = new Tuple(Plosca.velikost - 1, i);
		}
	}
	
	void printMap() {
		/* For debug. */
		
		System.out.printf("\n");
		for (int i = 0; i < Plosca.velikost; i++) {
			System.out.printf("-----");
		}
		System.out.printf("\n");
		
		for (int y = 0; y < Plosca.velikost; y++) {
			for (int x = 0; x < Plosca.velikost; x++) {
				int val = queryCell(new Tuple(x, y));
				System.out.printf("| %s |", val >= 0 ? val : ".");
			}
			System.out.printf("\n");
		}
		
		for (int i = 0; i < Plosca.velikost; i++) {
			System.out.printf("-----");
		}
		System.out.printf("\n");
	}
	
	private List<Tuple> queryTag(Integer tag) {
		/* Fetches cells which belong to eqv class. */
		return tagToCell.get(tag);
		
	}
	
	private int queryCell(Tuple cell) {
		/* Fetches eqv class to which cell belongs. */
		return cellToTag[cell.getX()][cell.getY()];
	}
	
	private List<Tuple> istobarvniSosedi(Tuple cell, Plosca plosca) {
		/* Returns neighbours of cell, which are of same colour as cell. */
		Polje barva = plosca.pridobiPolje(cell.getX(), cell.getY());
		List<Tuple> sosedi = plosca.pridobiSosede(cell.getX(), cell.getY());
		
		List<Tuple> istobarvni = new ArrayList<Tuple>();
		
		for (Tuple sosed : sosedi) {
			if (barva == plosca.pridobiPolje(sosed.getX(), sosed.getY())) {
				istobarvni.add(sosed);
			}
		}
		
		return istobarvni;
	}
	
	void addCellTo(Tuple cell, int tag) {
		/* Adds cell to eqv class. */
		cellToTag[cell.getX()][cell.getY()] = tag;
		tagToCell.get(tag).add(cell);
	}
	
	void insert(Tuple cell, Plosca plosca) {
		/* Inserts cell into appropriate eqv class, or creates a new one for it. */
		List<Tuple> istobarvni = istobarvniSosedi(cell, plosca);

		if (!istobarvni.isEmpty()) {
			/* If cell has same-colour neighbours. */
			List<Integer> tagiSosedov = new ArrayList<Integer>();
			
			/* Aggregate all equivalence classes which need merging. */
			for (Tuple sosed : istobarvni) {
				tagiSosedov.add(queryCell(sosed));
			}
			
			/* Merge eqv classes and add inserted cell into it. */
			addCellTo(cell, merge(tagiSosedov));
		} else {
			/* If cell is isolated we generate new eqv class with it in it. */
			addTag(cell);
		}
	}
	
	private int merge(List<Integer> tags) {
		/* Merges given eqvivalence classes. */
		Integer lead = tags.get(0);
		ArrayList<Tuple> lst = new ArrayList<Tuple>();
		for (Integer tag : tags) {
			lst.addAll(queryTag(tag));
		}
		
		for (Tuple cell : lst) {
			cellToTag[cell.getX()][cell.getY()] = lead;
		}
		
		tagToCell.set(lead, lst);
		
		return lead;
	}
	
	private void addTag(Tuple cell) {
		/* Generates a new eqvivalence class. */
		cellToTag[cell.getX()][cell.getY()] = nextTag;
		ArrayList<Tuple> lst = new ArrayList<Tuple>();
		lst.add(cell);
		tagToCell.add(lst);
		nextTag++;
		
	}
	
	boolean isWon(Polje barva, Plosca plosca) {
		/* Checks if player with barva has connected her edges. */
		
		/* Determine which edges need to be connected. */
		boolean is_modro = (barva == Polje.MODRO);
		Tuple[] rob_a = is_modro ? rob_modri_a : rob_rdeci_a;
		Tuple[] rob_b = is_modro ? rob_modri_b : rob_rdeci_b;

		/* Test if any pair of barva cells on barva edges belongs to same eqv class. */
		for (Tuple fst : rob_a) {
			if (barva != plosca.pridobiPolje(fst.getX(), fst.getY())) {
				continue;
			}
			for (Tuple snd : rob_b) {
				if (barva != plosca.pridobiPolje(snd.getX(), snd.getY())) {
					continue;
				}
				
				if (queryCell(fst) == queryCell(snd)) {
					/* If so game is won. */
					return true;
				}
			}
		}
		
		return false;
	}
}
