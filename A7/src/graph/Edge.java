package graph;
import gui.Line;

import java.awt.Color;
import java.util.*;

/** Class Edge allows creation of connections between Nodes.
 * Each Edge is bidirectional and is connected to exactly two Nodes. 
 * Functions getFirstExit() and getSecondExit() allow access to these Nodes. 
 * Each Edge is weighted (has a length). <br><br>
 * 
 * Class Edge implements GraphElement and is represented on the threads by an
 * instance of class Line.
 * <br><br>
 * 
 * Edge constructors are protected, so classes outside the package
 * cannot construct additional Edge objects.
 * 
 * @author MPatashnik
 *
 */
public final class Edge implements GraphElement{
    /**Max val an edge can have for length. */
    public static final int DEFAULT_MIN_LENGTH = Integer.MAX_VALUE;	

    /** Min val an edge can have for length. */
    public static final int DEFAULT_MAX_LENGTH = 0;

    private Node[] exits;	//The Nodes to which this edge connects. Always length 2.

    /** The length (weight) of this Edge. Uncorrelated with its graphical length on the GUI */
    public final int length;		

    private Line line; //Graphical representation of this Edge

    private final Graph graph;  //The graph tp which this Edge belongs

    /** Constructor. an Edge on m with end nodes in exits and length lengthOfRoad,
     * which must be positive and non-zero.
     * Throw an IllegalArgumentException if:
     * 		exits is null or has length != 2  OR
     * 		lengthOfRoad < 1 and != Edge.DUMMY_LENGTH  OR
     * 		either of the nodes are null  OR
     * 		exits[0] and exits[1] are the same node. */
    protected Edge(Graph m, Node exits[], int lengthOfRoad) throws IllegalArgumentException {
        this(m, exits[0], exits[1], lengthOfRoad);

        if (exits.length != 2)
            throw new IllegalArgumentException("Incorrectly sized Node Array Passed into Edge Constructor");
    }

    /** Constructor. An edge with end nodes firstExit and secondExit and  length
     * lengthOfRoad, which must be positive and non-zero.
     * Throw an IllegalArgumentException if:
     * 		lengthOfRoad <n 1 and != Edge.DUMMY_LENGTH  OR
     * 		either of the nodes are null OR
     * 		firstExit and secondExit are the same node. */
    protected Edge(Graph m, Node firstExit, Node secondExit, int lengthOfRoad) 
            throws IllegalArgumentException {
        Node[] e= new Node[2];
        e[0]= firstExit;
        e[1]= secondExit;
        setExits(e);
        
        graph= m;

        if (lengthOfRoad <= 0)
            throw new IllegalArgumentException("lengthOfRoad value " + lengthOfRoad + 
                    " is an illegal value.");

        length= lengthOfRoad;
        line= new Line(firstExit.getCircle(), secondExit.getCircle(), this);
    }

    /** Return the graph to which this Edge belongs. */
    @Override public Graph getGraph() {
        return graph;
    }

    /** Return the exits of this line, a length 2 array.
     * Order of nodes in returned array has no significance. */
    protected Node[] getTrueExits() {
        return exits;
    }

    /** Return the first exit of this Edge. Which node is the first exit has no significance. */
    public Node getFirstExit() {
        return exits[0];
    }

    /** Return the second exit of this Edge. Which node is the second exit has no significance. */
    public Node getSecondExit() {
        return exits[1];
    }

    /** Return a copy of the exits of this line, a new length 2 array of Nodes.
     * Copy the nodes into a new array to prevent interference with the exits of this node.
     * (Setting the values of the return of this method
     * will not alter the Edge object.) */
    public Node[] getExits() {
        return new Node[] {exits[0], exits[1]};
        //Node[] newExits= new Node[2];
        //newExits[0]= exits[0];
        //newExits[1]= exits[1];
        //return newExits;
    }

    /** Set the exists of this edge to newExits. Used only in edge construction.
     * THrow an IllegalArgumentException if:
     * 		exits is null or has length != 2  OR
     * 		either of the nodes are null  OR
     * 		exits[0] and exits[1] are the same node.  **/
    private void setExits(Node[] newExits) throws IllegalArgumentException{
        if (newExits == null)
            throw new IllegalArgumentException("Null Node Array Passed into Edge Constructor");

        if (newExits.length != 2)
            throw new IllegalArgumentException("Incorrectly sized Node Array Passed into Edge Constructor");

        if (newExits[0] == null)
            throw new IllegalArgumentException("First Node Passed into Edge constructor is null");

        if (newExits[1] == null)
            throw new IllegalArgumentException("Second Node Passed into Edge constructor is null");

        if (newExits[0].equals(newExits))
            throw new IllegalArgumentException("Two Nodes Passed into Edge constructor refer to the same node");

        exits= newExits;
    }

    /** Return true iff node is one of the exits of this Edge. */
    public boolean isExit(Node node) {
        return exits[0].equals(node) || exits[1].equals(node);
    }

    /** Return true iff this Edge and r share an exit. */
    public boolean sharesExit(Edge r) {
        if (exits[0].equals(r.getTrueExits()[0])) return true;
        if (exits[0].equals(r.getTrueExits()[1])) return true;
        if (exits[1].equals(r.getTrueExits()[0])) return true;
        if (exits[1].equals(r.getTrueExits()[1])) return true;
        return false;
    }

    /** Return the other exit that is not equal to n.
     *  (Return null if n is neither of the nodes in exits.) */
    public Node getOther(Node n) {
        if (exits[0].equals(n)) return exits[1];
        if (exits[1].equals(n)) return exits[0];
        return null;
    }

    /** Return the line that represents this edge graphically. */
    public Line getLine() {
        return line;
    }

    /** Return the color of this Edge, as it is painted on the GUI. Color of edges
     * has no game significance, so this value may be changed during gameplay. */
    @Override public Color getColor() {
        return line.getColor();
    }
    
    /** Return false - the color of Edges is not significant */
    @Override public boolean isColorSignificant(){
    	return false;
    }

    /** Return true iff this edge and e are equal.
     * Two Edges are equal if they have the same exits, even if they have different lengths.
     * This ensures that only one edge connects each pair of nodes in duplicate-free collections.  */
    @Override public boolean equals(Object e) {
        if (e == null) return false;
        if (!(e instanceof Edge)) return false;
        Edge e1= (Edge) e;
        Node[] exist1= e1.getTrueExits();
        return (exits[0].equals(exist1[1]) && exits[1].equals(exist1[0])) ||
               (exits[0].equals(exist1[0]) && exits[1].equals(exist1[1]));
    }

    /** Return the hash code for this edge.
     * The hashCode is equal to the sum of the hashCodes of its first and second exit.
     * {@code getFirstExit().hashCode() + getSecondExit().hashCode()}.
     * Notably: This means the ordering of the exits for an edge doesn't matter for hashing */
    @Override public int hashCode() {
        return exits[0].hashCode() + exits[1].hashCode();
    }

    /** Return a String representation of this edge:
     * {getFirstExit().name + " to " + getSecondExit().name} */
    @Override public String toString() {
        return exits[0].name + " to " + exits[1].name; 
    }

    /** Return the  exits and length for an edge's JSON string */
    @Override public String toJSONString() {
        return "{\n" + Main.addQuotes(GraphElement.LOCATION_TOKEN) + ":[" 
                + Main.addQuotes(exits[0].name) + "," + Main.addQuotes(exits[1].name) + "]," +
                "\n" + Main.addQuotes(GraphElement.LENGTH_TOKEN) + ":" + length + 
                "\n}";
    }

    /** Return a String to print when this object is drawn on a GUI */
    @Override public String getMappedName() {
        return "" + length;
    }

    /** Return the x location the name of this Edge relative to the top
     * left corner of the line */
    @Override public int getRelativeX() {
        return line.getXMid() - Math.min(line.getX1(), line.getX2()) + Line.LINE_THICKNESS;
    }

    /** Return the y location the name of this Edge relative to the top
     * left corner of the line */
    @Override public int getRelativeY() {
        return line.getYMid() - Math.min(line.getY1(), line.getY2()) + Line.LINE_THICKNESS*3;
    }


    /** Repaint the edge (the line). Parameters x and y unused but are included
     * to comply with interface. */
    @Override public void updateGUILocation(int x, int y) {
        getLine().fixBounds();
        getLine().repaint();
    }
}
