package graph;
import gui.Circle;
import gui.DraggableCircle;

import java.awt.Color;
import java.util.*;

/** A Node (vertex) of the graph. Each Node maintains
 *  (1) a set of edges that exit it,

 *  All methods that modify these collections are protected, but all getters
 *  are public for use by the user. Additionally, convenience methods such as
 *  isConnectedTo(Node n) are provided for user use.
 *  
 *  @author MPatashnik
 **/
public class Node implements GraphElement {
    /** The name of this node. Set during construction */
    public final String name;
    
    private final Graph graph; //The graph in which this Node is contained

    protected int x;	//x coordinate of this node in independent project space
    protected int y;	//y coordinate of this node in independent project space

    private List<Edge> exits;  //Edges leaving this Node

    private Object userData;

    private Circle circle;	//Circle that represents this graphically

    /** Constructor: a Node named n on Board m with no edges leaving it and
     * with drawing circle c. */
    protected Node(Graph m, String n, DraggableCircle c) {
        this(m, n, c, null);
    }

    /** Constructor: a Node named n on Board m with edges in exists and
     * with drawing circle c. If c is null, a new circle at (0, 0) is created. */
    protected Node(Graph m, String n, DraggableCircle c, List<Edge> exits) {
        graph= m;
        name= n;

        if (c == null)
            circle= new DraggableCircle(this, 0, 0, Circle.DEFAULT_DIAMETER);
        else
            circle= c;

        if (exits != null) {
            this.exits= Collections.synchronizedList(exits);
        } else {
            this.exits= Collections.synchronizedList(new ArrayList<Edge>());
        }
    }
    
    /** Return the graph in which this Node exists. */
    public Graph getGraph() {
        return graph;
    }

    /** Return the exits of this Node. */
    protected List<Edge> getTrueExits() {
        return exits;
    }

    /** Return a copy of the set of edges leaving this node.
     * This is to prevent the addition of edges to exits without proper clearance.
     */
    public List<Edge> getExits() {
        List<Edge> newExits= new ArrayList<Edge>();
        synchronized(exits) {
            newExits.addAll(exits);
        }
        return newExits;
    }

    /** Return a map of neighboring nodes to the lengths of the edges connecting
     *  them to this Node. To iterate over a HashMap, use HashMap.entrySet(). */
    public HashMap<Node, Integer> getNeighbors() {
        HashMap<Node, Integer> neighbors= new HashMap<Node, Integer>();
        synchronized(exits) {
            for (Edge e : exits) {
                neighbors.put(e.getOther(this), e.length);
            }
        }
        return neighbors;
    }

    /** Return a random edge leaving this node (its exists) */
    public Edge getRandomExit() {
        return Main.randomElement(exits);
    }

    /** Add e to this Node's set of exits */
    protected void addExit(Edge e) {
        if (!exits.contains(e)) {
        exits.add(e);
        }
    }

    /** Remove e from this Node's set of exits */
    protected void removeExit(Edge e) {
        exits.remove(e);
    }

    /** Add edges in s to this Node's set of edges (its exits). */
    protected void addExits(Collection<Edge> s) {
        for (Edge e : s) {
            if (!exits.contains(e)) {
               exits.add(e);
            }
        }
    }

    /** Return the number of exits from this node. */
    public int getExitsSize() {
        return exits.size();
    }

    /** Return true iff r is connected to this Node. */
    public boolean isExit(Edge r) {
        return exits.contains(r);
    }

    /** Create a new Edge with length len and add it as an exit
     * to this Node and node. */
    protected void connectTo(Node node, int len) {
        Edge r= new Edge(graph, this, node, len);
        addExit(r);
        node.addExit(r);
    }

    /** Return false if destination.equals(this). Otherwise,
     * Return true iff one of the edges in exits leads to Node destination, 
     * (this is connected to destination via a single edge). */
    public boolean isConnectedTo(Node destination) {
        if (destination.equals(this))
            return false;

        synchronized(exits) {
            for (Edge r : exits) {
                if (r.isExit(destination)) {
                    return true;
                }
            }
        }

        return false;
    }

    /** Return the edge that this node shares with node other (null if not connected). */
    public Edge getEdge(Node other) {
        Edge n= null;

        synchronized(exits) {
            for (Edge r : exits) {
                if (r.getOther(this).equals(other)) {
                    n= r;
                    break;
                }
            }
        }

        return n;
    }

    /** Return the Circle that represents this node graphically. */
    public Circle getCircle() {
        return circle;
    }

    /** Set the Circle for this Node to c. */
    public void setCircle(Circle c) {
        circle= c;
    }

    /** Update the circle graphic.
     * Also update the location of the load if this truck is carrying one. 
     * @param x - the new X location of this Truck in the GUI
     * @param y - the new Y location of this Truck in the GUI
     * */
    @Override public void updateGUILocation(int x, int y) {
        circle.setX1(x);
        circle.setY1(y);
        circle.repaint();
        for (Edge e : exits) {
            e.updateGUILocation(x, y);
        }
    }

    /** Return true iff n is a Node and is equal to this one.
     * Two Nodes are equal if they have the same name - guaranteed to be unique
     * within the context of a single game */
    @Override public boolean equals(Object n) {
        if (n == null)
            return false;
        if (!(n instanceof Node) )
            return false;
        return name.equals( ((Node)n).name);
    }

    /** Return the hashCode of this node. Its hashCode is equal to the hashCode of
     * its name. This is guaranteed to be unique within the context of a single game. */
    @Override public int hashCode() {
        return name.hashCode();
    }

    /** Return the color of this Node. */
    public Color getColor() {
        return circle.getColor();
    }

    /** Return false - the color of Nodes is not significant */
    @Override public boolean isColorSignificant(){
    	return false;
    }
    
    /** Return the name of this Node. */
    @Override public String toString() {
        return name;
    }

    /** Return the string that is mapped when this Node is drawn. */
    @Override
    public String getMappedName() {
        return name;
    }

    /** Return the x location that this Node's string is mapped to
     * relative to its top right coordinate. */
    @Override public int getRelativeX() {
        return -Circle.DEFAULT_DIAMETER/2;
    }

    /** Return the y location that this Node's string is mapped to
     * relative to its top right coordinate. */
    @Override public int getRelativeY() {
        return 0;
    }

    /** Return just this' name for the JSONString - relies on JSONs of Edges
     * and parcels to take care of themselves.  */
    @Override public String toJSONString() {
        return "{\n" + Main.addQuotes(GraphElement.NAME_TOKEN) + ":" +
                Main.addQuotes(name) + ",\n" +
                Main.addQuotes(X_TOKEN) + ":"+ x + ",\n" +
                Main.addQuotes(Y_TOKEN) + ":" + y + "\n}";
    }
}
