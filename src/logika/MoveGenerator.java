package logika;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MoveGenerator {
	/* Holds available moves. We only need fast remove (when move is made) and getRandomMove 
	 * (since we are using Monte-Carlo methods in engine). getAll can be slower, since
	 * it won't be used as much.
	 */
	private int[][] hash_matrix; /* Maps x, y to index in move_array. */
	
	/* If we cared about these few bytes of memory we would use ArrayList. */
	private Tuple[] move_array; /* Holds free moves. */
	private int move_array_end; /* Holds last used index. */
	
	/* PRNG */
	private Random prng;
	
	MoveGenerator() {
		int w = Plosca.velikost;
		
		move_array_end = w * w - 1;
		move_array = new Tuple[w * w];
		hash_matrix = new int[w][w];
		
		prng = new Random();
		
		/* At first all moves are available, so we can use natural indexes. */
		for (int x = 0; x < Plosca.velikost; x++) {
			for (int y = 0; y < Plosca.velikost; y++) {
				int index = y * w + x;
				hash_matrix[x][y] = index;
				move_array[index] = new Tuple(x, y);
			}
		}
	}
	
	MoveGenerator(MoveGenerator toBeCopied) {
		hash_matrix = new int[Plosca.velikost][Plosca.velikost];
		move_array_end = toBeCopied.move_array_end;
		
		move_array = new Tuple[Plosca.velikost * Plosca.velikost + 1];
		prng = new Random();
		
		for (int x = 0; x < Plosca.velikost; x++) {
			for (int y = 0; y < Plosca.velikost; y++) {
				int index = toBeCopied.hash_matrix[x][y];
				hash_matrix[x][y] = index;
				move_array[index] = new Tuple(x, y);
			}
		}
	}
	
	public void remove(Tuple element) {
		
		/* O(1) */
		
		/* Get current index of element to be removed. */
		int x = element.getX();
		int y = element.getY();
		int i = hash_matrix[x][y];
		
		/* Special cases. */
		if (i > move_array_end) {
			/* Item was already deleted. */
			return;
		}
		
		if (i == move_array_end) {
			/* Removing last element is trivial. */
			move_array_end--;
			return;
		}
		
		/* Get last available move in array. */
		Tuple last_el = move_array[move_array_end];

		int lx = last_el.getX();
		int ly = last_el.getY();
		
		/* Swap them and shrink array for 1. */
		hash_matrix[lx][ly] = i;
		hash_matrix[x][y] = move_array_end; 
		
		
		move_array[i] = last_el;
		move_array[move_array_end] = element;
		
		move_array_end--;
	}
	
	public List<Tuple> getAll() {
		return Arrays.asList(Arrays.copyOfRange(move_array, 0, move_array_end + 1));
	}
	
	public Tuple getRandom() {
		Tuple mov = move_array[prng.nextInt(move_array_end + 1)];
		
		return mov;
	}
}
