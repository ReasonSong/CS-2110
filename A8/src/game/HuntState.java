package game;

import java.util.Collection;

/** The state of the game while performing exploration.
 * In order to determine the next move you need to call the various methods
 * of this interface. To move through the cavern, you need to call moveTo(long).
 *
 * An instance provides all of the information necessary
 * to search through the cavern and find the Orb.
 */
public interface HuntState {
    /** Return the unique identifier associated with Indiana's current location. */
    public long currentLocation();

    /** Return an unordered collection of NodeStatus objects
     * associated with all direct neighbors of Indiana's current location.
     * Each status contains a unique identifier for the neighboring node
     * as well as the distance of that node to the Orb along the grid<br><br>
     * (NB: This is NOT the distance in the graph, it is only the number
     *      of rows and columns away from the Orb.)<br><br>
     * It is possible to move directly to any node identifier in this collection.
     */
    public Collection<NodeStatus> neighbors();

    /** Return Indiana's current distance along the grid (NOT THE GRAPH) from
     * the Orb. */
    public int distanceToOrb();

    /** Change Indiana's current location to the node given by id.<br><br>
     * Throw an IllegalArgumentException if the node with id id is not
     *    adjacent to Indiana's current location. */
    public void moveTo(long id);
}
