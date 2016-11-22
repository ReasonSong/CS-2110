package game;

import java.util.Collection;

/** Scram means to get out, to run away.
 * A ScramState provides all the information necessary to
 * get out of the cavern and collect gold on the way.
 * 
 * This interface provides access to the complete graph of the cavern,
 * which will allow computation of the path.
 * Once you have determined how Indiana should get out, call
 * moveTo(Node) repeatedly to move to each node and grabGold() to collect
 * gold on the way out. */
public interface ScramState {
    /** Return the Node corresponding to Indiana's location in the graph. */
    public Node currentNode();

    /** Return the Node associated with the exit from the cavern.
     * Indiana has to move to this Node in order to get out. */
    public Node getExit();

    /** Return a collection containing all the nodes in the graph.
     * They in no particular order. */
    public Collection<Node> allNodes();

    /** Change Indiana's location to n.
     * Throw an IllegalArgumentException if n is not directly connected to
     * Indiana's location. */
    public void moveTo(Node n);

    /** Pick up the gold on the current tile.
     * Throw an IllegalStateException if there is no gold at the current location,
     *     either because there never was any or because it was already picked up. */
    public void grabGold();

    /** Return the steps remaining to get out of the cavern.
     * This value will change with every call to moveTo(Node),
     * and if it reaches 0 before you get out, you have failed to get out.  */
    public int stepsLeft();
}
