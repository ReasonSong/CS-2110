package gui;

import controller.Controller;
import model.Game;
import model.Location;
import model.Player;

public class GUIController extends Controller {
	
	private int x, y;	// player's current move location 
	private int lastX, lastY;	// player's last move location
	private Game game;
	
	public GUIController(Player me, Game g) {
		super(me);
		x = -1;
		y = -1;
		lastX = x;
		lastY = y;
		this.game = g;
	}
	
	public void updateLoc(int x, int y){
		this.x = x;
		this.y = y;
		gameChanged(game);
	}
	
	@Override
	protected Location nextMove(Game g) {
		if (x == lastX && y == lastY) return null;	// the player hasn't made his move
		lastX = x; lastY = y;	// update the player's move
		return new Location(x, y);
	}
}
