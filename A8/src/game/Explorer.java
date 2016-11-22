package game;

/** An abstract class representing what methods an explorer
 *  must implement in order to be used in solving the game.
 */
public abstract class Explorer {
	
	 /** 
     * Explore the cavern, trying to find the 
     * orb in as few steps as possible. Once you find the 
     * orb, you must return from huntOrb in order to pick
     * it up. If you continue to move after finding the orb rather 
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb, 
     * it will count as a failure.
     * 
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * 
     * At every step, you only know your current tile's ID and the ID of all 
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles). 
     * 
     * In order to get information about the current state, use functions
     * getCurrentLocation(), getNeighbors(), and getDistanceToTarget() in huntState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * 
     * Use function moveTo(long id) in huntState to move to a neighboring 
     * tile by its ID. Doing this will change state to reflect your new position.
     * 
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     * 
     * @param state the information available at the current state
     */
    public abstract void huntOrb(HuntState state);

    /** Get out of the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS get out before time runs
     * out, and this should be prioritized above collecting gold.
     * 
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * currentNode() and getExit() returns Node objects of interest, and getNodes()
     * returns a collection of all nodes on the graph. 
     * 
     * Note that time is measured entirely in the number of steps taken. For each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * 
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * 
     * You will always have enough time to get out using the shortest path from the starting
     * position to the exit, although this will not collect much gold. But for this reason, using 
     * Dijkstra's to plot the shortest path to the exit is a good starting solution.
     * 
     * @param state the information available at the current state
     */
    public abstract void scram(ScramState state);
}
