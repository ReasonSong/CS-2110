package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import gui.GUI;

import student.Indiana;

public class GameState implements HuntState, ScramState {

    private enum Stage {
        EXPLORE, ESCAPE;
    }

    @SuppressWarnings("serial")
    private static class OutOfTimeException extends RuntimeException {
    }
    
    static boolean shouldPrint = true;

    public static final int MIN_ROWS = 8;
    public static final int MAX_ROWS = 25;
    public static final int MIN_COLS = 12;
    public static final int MAX_COLS = 40;
    
    public static final long EX_TIMEOUT = 10;
    public static final long ES_TIMEOUT = 15;

    public static final double MIN_BONUS = 1.0;
    public static final double MAX_BONUS = 1.3;

    private static final double EXTRA_TIME_FACTOR = 0.3;     //bigger is nicer - addition to total multiplier
    private static final double NO_BONUS_LENGTH = 3;

    private final Cavern exploreCavern;
    private final Cavern escapeCavern;

    private final Explorer explorer;
    private final Optional<GUI> gui;

    private final long seed;

    private Node position;
    private int stepsTaken;
    private int timeRemaining;
    private int goldCollected;

    private Stage stage;
    private boolean exploreSucceeded= false;
    private boolean escapeSucceeded= false;
    private boolean exploreErrored= false;
    private boolean escapeErrored= false;
    private boolean exploreTimedOut= false;
    private boolean escapeTimedOut= false;

    private int minExploreDistance;
    private int minEscapeDistance;

    private int exploreDistanceLeft= 0;
    private int escapeDistanceLeft= 0;

    private int minTimeToExplore;

    /** Constructor: a new GameState object for explorer exp. This constructor takes a path to files
     *  storing serialized caverns and simply loads these caverns.
     */
    /* package */ GameState(Path exploreCavernPath, Path escapeCavernPath, Explorer exp) throws IOException {
        exploreCavern= Cavern.deserialize(Files.readAllLines(exploreCavernPath));
        minTimeToExplore= exploreCavern.minPathLengthToTarget(exploreCavern.getEntrance());
        escapeCavern= Cavern.deserialize(Files.readAllLines(escapeCavernPath));

        explorer= exp;
       

        position= exploreCavern.getEntrance();
        stepsTaken= 0;
        timeRemaining= Integer.MAX_VALUE;
        goldCollected= 0;

        seed= -1;

        stage= Stage.EXPLORE;
        gui= Optional.of(new GUI(exploreCavern, position.getTile().getRow(), 
                position.getTile().getColumn(), 0));
    }

    /** Constructor: a new random game instance with or without a GUI. */
    private GameState(boolean useGui, Explorer exp) {
        this((new Random()).nextLong(), useGui, exp);
    }
    
    /** Constructor: a new game instance using seed seed with or without a GUI,
     *  and with the explorer used to solve the game. */
    /* package */ GameState(long seed, boolean useGui, Explorer exp) {
        Random rand = new Random(seed);
        int ROWS = rand.nextInt(MAX_ROWS - MIN_ROWS + 1) + MIN_ROWS; 
        int COLS = rand.nextInt(MAX_COLS - MIN_COLS + 1) + MIN_COLS;
        exploreCavern= Cavern.digExploreCavern(ROWS, COLS, rand);
        minTimeToExplore= exploreCavern.minPathLengthToTarget(exploreCavern.getEntrance());
        Tile orbTile = exploreCavern.getTarget().getTile();
        escapeCavern= Cavern.digEscapeCavern(ROWS, COLS,  orbTile.getRow(), orbTile.getColumn(), rand);

        position= exploreCavern.getEntrance();
        stepsTaken= 0;
        timeRemaining= Integer.MAX_VALUE;
        goldCollected= 0;
        
        explorer= exp;
        stage= Stage.EXPLORE;

        this.seed = seed;

        if (useGui) {
            gui= Optional.of(new GUI(exploreCavern, position.getTile().getRow(),
                    position.getTile().getColumn(), seed));
        } else {
            gui= Optional.empty();
        }
    }

    /** Run through the game, one step at a time.
     *  Will  run escape() only if explore() succeeds.
     *  Will fail in case of timeout.  */
    void runWithTimeLimit() {
    	exploreWithTimeLimit();
        if (!exploreSucceeded) {
            exploreDistanceLeft= exploreCavern.minPathLengthToTarget(position);
            escapeDistanceLeft = escapeCavern.minPathLengthToTarget(escapeCavern.getEntrance());
        }
        else{
	        escapeWithTimeLimit();
	        if (!escapeSucceeded) {
	            escapeDistanceLeft= escapeCavern.minPathLengthToTarget(position);
	        }
        }
    }
    
    /** Run through the game, one step at a time.
     *  Will run escape() only if explore() succeeds.
     *  Does not use a timeout and will wait as long as necessary.
     */
    void run(){
    	explore();
    	if (!exploreSucceeded){
    		exploreDistanceLeft= exploreCavern.minPathLengthToTarget(position);
            escapeDistanceLeft = escapeCavern.minPathLengthToTarget(escapeCavern.getEntrance());
    	}
    	else{
    		escape();
    		if (!escapeSucceeded) {
	            escapeDistanceLeft= escapeCavern.minPathLengthToTarget(position);
	        }
    	}
    }
    
    /** Run only the explore mode. Uses timeout. */
    void runExploreWithTimeout(){
    	exploreWithTimeLimit();
        if (!exploreSucceeded) {
            exploreDistanceLeft= exploreCavern.minPathLengthToTarget(position);
        }
    }
    
    /** Run only the escape mode. Uses timeout. */
    void runEscapeWithTimeout(){
        escapeWithTimeLimit();
        if (!escapeSucceeded) {
            escapeDistanceLeft= escapeCavern.minPathLengthToTarget(position);
        }
    }
    
    @SuppressWarnings("deprecation")
    /** Wraps a call to explore() with the timeout functionality. */
	private void exploreWithTimeLimit() {
    	FutureTask<Void> ft = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() {
				explore();
				return null;
			}
		});

		Thread t = new Thread(ft);
		t.start();
		try {
			ft.get(EX_TIMEOUT, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			t.stop();
			exploreTimedOut= true;
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("ERROR");
			// Shouldn't happen
		}
	}

    /** Run the explorer's explore() function with no timeout. */
    /* package */ void explore() {
        stage= Stage.EXPLORE;
        stepsTaken= 0;
        exploreSucceeded= false;
        position= exploreCavern.getEntrance();
        minExploreDistance= exploreCavern.minPathLengthToTarget(position);
        gui.ifPresent((g) -> g.setLighting(false));
        gui.ifPresent((g) -> g.updateCavern(exploreCavern, 0));
        gui.ifPresent((g) -> g.moveTo(position));

        try {
            explorer.huntOrb(this);
        	//Verify that we returned at the correct location
            if (position.equals(exploreCavern.getTarget())) {
                exploreSucceeded= true;
            } else {
                errPrintln("Your solution to explore returned at the wrong location.");
                gui.ifPresent((g) -> g.displayError("Your solution to explore returned at the wrong location."));
            }
        } catch (Throwable t) {
        	if (t instanceof ThreadDeath) return;
            errPrintln("Your code errored during the explore phase.");
            gui.ifPresent((g) -> g.displayError("Your code errored during the explore phase. Please see console output."));
            errPrintln("Here is the error that occurred.");
            t.printStackTrace();
            exploreErrored= true;
        }
    }

    @SuppressWarnings("deprecation")
    /** Wraps a call to escape() with the timeout functionality. */
	private void escapeWithTimeLimit() {
    	FutureTask<Void> ft = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() {
				escape();
				return null;
			}
		});

		Thread t = new Thread(ft);
		t.start();
		try {
			ft.get(ES_TIMEOUT, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			t.stop();
			escapeTimedOut= true;
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("ERROR");
			// Shouldn't happen
		}
	}
    
    /** Handle the logic for running the explorer's escape() procedure with no timeout. */
    /* package */ void escape() {
        stage= Stage.ESCAPE;
        Tile orbTile= exploreCavern.getTarget().getTile();
        position= escapeCavern.getNodeAt(orbTile.getRow(), orbTile.getColumn());
        minEscapeDistance= escapeCavern.minPathLengthToTarget(position);
        timeRemaining= computeTimeToEscape();
        gui.ifPresent((g) -> g.setLighting(true));
        gui.ifPresent((g) -> g.updateCavern(escapeCavern, timeRemaining));

        try {
            explorer.scram(this);
        	//Verify that we returned at the correct location
            if (position.equals(escapeCavern.getTarget())) {
                escapeSucceeded= true;
            }
            else {
                errPrintln("Your solution to escape returned at the wrong location.");
                gui.ifPresent((g) -> g.displayError("Your solution to escape returned at the wrong location."));
            }
        } catch (OutOfTimeException e) {
            errPrintln("Your solution to escape ran out of steps before returning!");
            gui.ifPresent((g) -> g.displayError("Your solution to escape ran out of steps before returning!"));
        } catch (Throwable t) {
        	if (t instanceof ThreadDeath) return;
            errPrintln("Your code errored during the escape phase.");
            gui.ifPresent((g) -> g.displayError("Your code errored during the escape phase. Please see console output."));
            t.printStackTrace();
            escapeErrored= true;
        }

        outPrintln("Gold collected   : " + getGoldCollected());
        DecimalFormat df = new DecimalFormat("#.##");
        outPrintln("Bonus multiplier : " + df.format(computeBonusFactor()));
        outPrintln("Score            : " + getScore());
    }

    /** Making sure the explorer always has the minimum time needed to escape, add a 
     *  factor of extra time proportional to the size of the cavern.
     */
    private int computeTimeToEscape() {
        int minTimeToEscape= escapeCavern.minPathLengthToTarget(position);
        return (int)(minTimeToEscape + EXTRA_TIME_FACTOR * 
        		(Cavern.MAX_EDGE_WEIGHT + 1) * escapeCavern.numOpenTiles() / 2);

    }

    /** Compare the explorer's performance on the explore() stage to the theoretical minimum,
     *  compute their bonus factor on a call frm MIN_BONUS to MAX_BONUS.
     *  Bonus should be minimum if take longer than NO_BONUS_LENGTH times optimal.
     */
    private double computeBonusFactor(){
        double exploreDiff = (stepsTaken - minTimeToExplore) / (double) minTimeToExplore;
        if (exploreDiff <= 0) return MAX_BONUS;
        double multDiff = MAX_BONUS - MIN_BONUS;
        return Math.max(MIN_BONUS, MAX_BONUS - exploreDiff / NO_BONUS_LENGTH * multDiff);
    }

    /** See moveTo(Node&lt;TileData&gt; n)
     *
     * @param id The Id of the neighboring Node to move to */
    @Override public void moveTo(long id) {
        if (stage != Stage.EXPLORE) {
            throw new IllegalStateException("moveTo(ID) can only be called while exploring!");
        }

        for (Node n : position.getNeighbors()) {
            if (n.getId() == id) {
                position = n;
                stepsTaken++;
                gui.ifPresent((g) -> g.updateBonus(computeBonusFactor()));
                gui.ifPresent((g) -> g.moveTo(n));
                return;
            }
        }
        throw new IllegalArgumentException("moveTo: Node must be adjacent to position");
    }

    /**  Return the unique id of the current location. */
    @Override public long currentLocation() {
        if (stage != Stage.EXPLORE) {
            throw new IllegalStateException("getLocation() can only be called while exploring!");
        }

        return position.getId();
    }

    /**  Return a collection of NodeStatus objects that contain the unique ID of the node
     * and the distance from that node to the target. */
    @Override public Collection<NodeStatus> neighbors() {
        if (stage != Stage.EXPLORE) {
            throw new IllegalStateException("getNeighbors() can only be called while exploring!");
        }

        Collection<NodeStatus> options= new ArrayList<>();
        for (Node n : position.getNeighbors()) {
            int distance= computeDistanceToTarget(n.getTile().getRow(), n.getTile().getColumn());
            options.add(new NodeStatus(n.getId(), distance));
        }
        return options;
    }

    /** Return the Manhattan distance from (row, col) to the target */
    private int computeDistanceToTarget(int row, int col) {
        return Math.abs(row - exploreCavern.getTarget().getTile().getRow())
                + Math.abs(col - exploreCavern.getTarget().getTile().getColumn());
    }

    /** Return the Manhattan distance from the current location to the target location on the map. */
    @Override public int distanceToOrb() {
        if (stage != Stage.EXPLORE) {
            throw new IllegalStateException("getDistanceToTarget() can only be called while exploring!");
        }

        return computeDistanceToTarget(position.getTile().getRow(), position.getTile().getColumn());
    }

    @Override
    public Node currentNode() {
        if (stage != Stage.ESCAPE) {
            throw new IllegalStateException("getCurrentNode: Error, " +
                    "current Node may not be accessed unless in ESCAPE");
        }
        return position;
    }

    @Override public Node getExit() {
        if (stage != Stage.ESCAPE) {
            throw new IllegalStateException("getEntrance: Error, "+
                    "current Node may not be accessed unless in ESCAPE");
        }
        return escapeCavern.getTarget();
    }

    @Override public Collection<Node> allNodes() {
        if (stage != Stage.ESCAPE) {
            throw new IllegalStateException("getVertices: Error, "+
                    "Vertices may not be accessed unless in ESCAPE");
        }
        return Collections.unmodifiableSet(escapeCavern.getGraph());
    }

    /** Attempt to move the explorer from the current position to
     * the <tt>Node</tt> <tt>n</tt>. Throws an <tt>IllegalArgumentException</tt>
     * if <tt>n</tt> is not neighboring. Increment the steps taken
     * if successful. */
    @Override public void moveTo(Node n) {
        if (stage != Stage.ESCAPE) {
            throw new IllegalStateException("moveTo(Node) can only be called when escaping!");
        }
        int distance = position.getEdge(n).length;
        if (timeRemaining - distance < 0) {
            throw new OutOfTimeException();
        }

        if (position.getNeighbors().contains(n)) {
            position= n;
            timeRemaining-=distance;
            gui.ifPresent((g) -> g.updateTimeLeft(timeRemaining));
            gui.ifPresent((g) -> g.moveTo(n));
        } else {
            throw new IllegalArgumentException("moveTo: Node must be adjacent to position");
        }
    }

    @Override public void grabGold() {
        if (stage != Stage.ESCAPE) {
            throw new IllegalStateException("pickUpGold() can only be called while escaping!");
        }
        else if (position.getTile().gold() <= 0) {
            throw new IllegalStateException("pickUpGold: Error, no gold on this tile");
        }
        goldCollected += position.getTile().takeGold();
        gui.ifPresent((g) -> g.updateCoins(goldCollected, getScore()));
    }

    @Override public int stepsLeft() {
        if (stage != Stage.ESCAPE) {
            throw new IllegalStateException("getTimeRemaining() can only be called while escaping!");
        }
        return timeRemaining;
    }

    /* package */ int getGoldCollected() {
        return goldCollected;
    }

    /** Return the player's current score.  */
    /* package */ int getScore() {
        return (int)(computeBonusFactor() * goldCollected);
    }

    /* package */ boolean getExploreSucceeded() {
        return exploreSucceeded;
    }

    /* package */ boolean getEscapeSucceeded() {
        return escapeSucceeded;
    }

    /* package */ boolean getExploreErrored() {
        return exploreErrored;
    }

    /* package */ boolean getEscapeErrored() {
        return escapeErrored;
    }
    
    /* package */ boolean getExploreTimeout() {
        return exploreTimedOut;
    }

    /* package */ boolean getEscapeTimeout() {
        return escapeTimedOut;
    }

    /* package */ int getMinExploreDistance() {
        return minExploreDistance;
    }

    /* package */ int getMinEscapeDistance() {
        return minEscapeDistance;
    }

    /* package */ int getExploreDistanceLeft() {
        return exploreDistanceLeft;
    }

    /* package */ int getEscapeDistanceLeft() {
        return escapeDistanceLeft;
    }
    
    /** Given seed, whether or not to use the GUI, and an instance of a solution to use,
     *  run the game.
     */
    public static int runNewGame(long seed, boolean useGui, Explorer solution) {
        GameState state;
        if (seed != 0) {
            state= new GameState(seed, useGui, solution);
        } else {
            state= new GameState(useGui, solution);
        }
        outPrintln("Seed : " + state.seed);
        state.run();
        return state.getScore();
    }

    public static void main(String[] args) throws IOException {
        List<String> argList= new ArrayList<String>(Arrays.asList(args));
        int repeatNumberIndex= argList.indexOf("-n");
        int numTimesToRun= 1;
        if (repeatNumberIndex >= 0) {
            try {
                numTimesToRun= Math.max(Integer.parseInt(argList.get(repeatNumberIndex + 1)), 1);
            }
            catch (Exception e) {
                // numTimesToRun = 1
            }
        }
        int seedIndex = argList.indexOf("-s");
        long seed= 0;
        if (seedIndex >= 0) {
            try {
                seed= Long.parseLong(argList.get(seedIndex + 1));
            }
            catch (NumberFormatException e) {
                errPrintln("Error, -s must be followed by a numerical seed");
                return;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                errPrintln("Error, -s must be followed by a seed");
                return;
            }
        }

        int totalScore= 0;
        for (int i= 0; i < numTimesToRun; i++) {
            totalScore += runNewGame(seed, false, new Indiana());
            if (seed != 0) seed = new Random(seed).nextLong();
            outPrintln("");
        }

        outPrintln("Average score : " + totalScore / numTimesToRun);
    }
    
    static void outPrintln(String s) {
    	if (shouldPrint) System.out.println(s);
    }
    
    static void errPrintln(String s) {
    	if (shouldPrint) System.err.println(s);
    }
}
