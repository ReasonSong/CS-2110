package controller;

import java.util.*;

import model.Board;
import model.Board.State;
import model.Game;
import model.Location;
import model.Player;
import model.Victory;

/**
 * A MinMaxAI is a controller that uses the minimax algorithm to select the next
 * move.  The minimax algorithm searches for the best possible next move, under
 * the assumption that your opponent will also always select the best possible
 * move.
 *
 * <p>The minimax algorithm assigns a score to each possible game configuration
 * g.  The score is assigned recursively as follows: if the game g is over and
 * the player has won, then the score is infinity.  If the game g is over and
 * the player has lost, then the score is negative infinity.  If the game is a
 * draw, then the score is 0.
 *
 * <p>If the game is not over, then there are many possible moves that could be
 * made; each of these leads to a new game configuration g'.  We can
 * recursively find the score for each of these configurations.
 *
 * <p>If it is the player's turn, then they will choose the action that
 * maximizes their score, so the score for g is the maximum of all the scores
 * of the g's.  However, if it is the opponent's turn, then the opponent will
 * try to minimize the score for the player, so the score for g is the
 * <em>minimum</em> of all of the scores of the g'.
 *
 * <p>You can think of the game as defining a tree, where each node in the tree
 * represents a game configuration, and the children of g are all of the g'
 * reachable from g by taking a turn.  The minimax algorithm is then a
 * particular traversal of this tree.
 *
 * <p>In practice, game trees can become very large, so we apply a few
 * strategies to narrow the set of paths that we search.  First, we can decide
 * to only consider certain kinds of moves.  For five-in-a-row, there are
 * typically at least 70 moves available at each step; but it's (usually) not
 * sensible to go on the opposite side of the board from where all of the other
 * pieces are; by restricting our search to only part of the board, we can
 * reduce the space considerably.
 *
 * <p>A second strategy is that we can look only a few moves ahead instead of
 * planning all the way to the end of the game.  This requires us to be able to
 * estimate how "good" a given board looks for a player.
 *
 * <p>This class implements the minimax algorithm with support for these two
 * strategies for reducing the search space.  The abstract method {@link
 * #moves(Board)} is used to list all of the moves that the AI is willing to
 * consider, while the abstract method {@link #estimate(Board)} returns
 * the estimation of how good the board is for the given player.
 */
public abstract class MinMaxAI extends Controller {
	private final int depth;
	private gui.Board b = null;
	/**
	 * Return an estimate of how good the given board is for me.
	 * A result of infinity means I have won.  A result of negative infinity
	 * means that I have lost.
	 */
	protected abstract int estimate(Board b);

	/**
	 * Return the set of moves that the AI will consider when planning ahead.
	 * Must contain at least one move if there are any valid moves to make.
	 */
	protected abstract Iterable<Location> moves(Board b);

	/**
	 * Create an AI that will recursively search for the next move using the
	 * minimax algorithm.  When searching for a move, the algorithm will look
	 * depth moves into the future.
	 *
	 * <p>choosing a higher value for depth makes the AI smarter, but requires
	 * more time to select moves.
	 */
	protected MinMaxAI(Player me, int depth) {
		super(me);
		this.depth = depth;
	}

	protected MinMaxAI(Player me, int depth, gui.Board board) {
		super(me);
		this.depth = depth;
		this.b = board;
	}


	/**
	 * Return the move that maximizes the score according to the minimax
	 * algorithm described above.
	 */
	protected @Override Location nextMove(Game g) {

		int d = this.depth;		// what's the meaning of creating this variable d ?
		int[] result = minMax(d, g.getBoard());
		Location loc = new Location(result[1], result[2]);
		if (b != null) { 	// Did I change the style here ?
			b.getSquare(result[1], result[2]).mark();	// Did I do some change here ?
		}																						// If I did, why ?
		return loc;
	}

	private int[] minMax(int depth, Board b) {
		Iterable<Location> result = new ArrayList<>();
		int score = (depth%2==0) ? Integer.MIN_VALUE:Integer.MAX_VALUE;
		int currentScore;
		int r = -1, c = -1;
		int[] best = {score, r, c};
		result = moves(b);

		if (b.getState() == State.HAS_WINNER) {
			Victory win = b.getWinner();
			if (win.equals(this.me)) {
				return new int[]{Integer.MAX_VALUE, -1, -1};
			} else {
				return new int[]{Integer.MIN_VALUE, -1, -1};
			}
		}

		if (b.getState() == State.DRAW) {
			return new int[]{0, -1, -1};
		}

		if (depth == 0) {
			score = estimate(b);	// Did I put a return here ? Why?
		} else {	// Is this 'else' necessary ?
			for (Location loc : result) {
				if (depth%2 == 0) {
					currentScore = minMax(depth-1, b.update(this.me, loc))[0];
					if (currentScore > score) {
						score = currentScore;		// Are these
						r = loc.row;						// three lines similar to
						c = loc.col;						// any lines below ?
					}
				} else {
					currentScore = minMax( depth-1, b.update(this.me.opponent(), loc))[0];
					if (currentScore < score) {
						score = currentScore;
						r = loc.row;
						c = loc.col;
					}
				}
			}
		}
		// You said you had gone through the push I pushed about refining your
		// coding style, and you revert "every" change I made back,
		// so what exactly do you mean ?
		return new int[] {score, r, c};
	}
}
