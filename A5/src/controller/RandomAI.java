package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.*;

/**
 * A RandomAI is a Controller that always chooses a random blank cell to play.
 */
public class RandomAI extends Controller {

	private final Random random;
	private gui.Board b = null;
	
	public RandomAI(Player me) {
		super(me);
		this.random = new Random();
	}
	
	public RandomAI(Player me, gui.Board b) {
		super(me);
		this.random = new Random();
		this.b = b;
	}

	@Override
	protected Location nextMove(Game g) {
		List<Location> available = new ArrayList<Location>();
		
		// find available moves
		for (Location loc : Board.LOCATIONS)
			if (g.getBoard().get(loc) == null)
				available.add(loc);
		
		// wait a bit
		delay();

		// choose a random move
		if (!available.isEmpty()){
			Location loc = available.get(random.nextInt(available.size()));
			if (b != null)b.getSquare(loc.row, loc.col).mark();
			return loc;
		}

		return null;
	}

}
