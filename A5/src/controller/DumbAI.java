package controller;

import model.Board;
import model.Game;
import model.Location;
import model.Player;

/**
 * A DumbAI is a Controller that always chooses the blank space with the
 * smallest column number from the row with the smallest row number.
 */
public class DumbAI extends Controller {

	private gui.Board b = null;
	
	public DumbAI(Player me) {
		super(me);
	}
	
	
	public DumbAI(Player me, gui.Board board) {
		super(me);
		this.b = board;
	}

	protected @Override Location nextMove(Game g) {

		// find available moves
		for (Location loc : Board.LOCATIONS)
			if (g.getBoard().get(loc) == null) {
				delay();
				if (b != null) b.getSquare(loc.row, loc.col).mark();
				return loc;
			}
		
		return null;
	}
}
