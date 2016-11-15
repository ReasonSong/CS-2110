/* Time spent on a7:  01 hours and 20 minutes.

 * Name: Ruochen Song, Hongshu Ye
 * Netid: rs2352, hy483
 * What I thought about this assignment:
 *
 *
 */

package student;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Edge;
import graph.Node;
import heap.Heap;

/** This class contains Dijkstra's shortest-path algorithm and some other methods. */
public class Paths {

	/** Return a list of the nodes on the shortest path from start to 
     * end, or the empty list if a path does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
    public static List<Node> shortestPath(Node start, Node end) {
        /* TODO Implement Dijkstras's shortest-path algorithm presented
         in the slides titled "Final Algorithm" (slide 37 or so) in the slides
         for lecture 20.
         In particular, a min-heap (as implemented in assignment A6) should be
         used for the frontier set. We provide a declaration of the frontier.
         
         We will make our solution to min-heap available as soon as the deadline
         for submission has passed.

         Maintaining information about shortest paths will require maintaining
         for each node in the settled and frontier sets the backpointer for
         that node, as described in the A7 handout, along with the length of the
         shortest path (thus far, for nodes in the frontier set). For this
         purpose, we provide static class SFdata.
         
         You have to declare the HashMap field for and describe carefully what it
         means. WRITE A PRECISE DEFINITION OF IT.

         Note 1: Read the notes on pages 2..3 of the handout carefully.
         Note 2: The method MUST return as soon as the shortest path to node
                 end has been calculated. In that case, do NOT continue to find
                 shortest paths. You will lost 15 points if you do not do this.
         Note 3: the only graph methods you may call are these:

            1. n.getExits():  Return a List<Edge> of edges that leave Node n.
            2. e.getOther(n): n must be one of the Nodes of Edge e.
                              Return the other Node.
            3. e.length:    Return the length of Edge e.
		
         Method pathDistance uses one more: n1.getEdge(n2)
         */
    	
    	//The HashMap local variable stores the entries for nodes in S or F as keys
    	//and the corresponding SFdate of the nodes as values
    	HashMap<Node, SFdata> map = new HashMap<Node, SFdata>(); 
    	Heap<Node> frontier = new Heap<Node>();
    	frontier.add(start, 0.0);
    	SFdata sfd = new SFdata(0, null);
    	map.put(start, sfd);
    	
    	while(frontier.size() != 0) {
    		Node f = (Node) frontier.poll();
    		if (f.equals(end)) {
    			return constructPath(end, map);
    		}
    		
    		List<Edge> edge = f.getExits();
    		for (Edge e : edge) {
    			Node w = e.getOther(f);
    			if(!map.containsKey(w)){
    				int distance = map.get(f).distance + e.length;
    				frontier.add(w, (double) distance);
    				SFdata data = new SFdata(distance, f);
    				map.put(w, data);
    			} else { 
    				if (map.get(f).distance + e.length < map.get(w).distance) {
    					int distance = map.get(f).distance + e.length;
    					map.get(w).distance = distance;
    					map.get(w).backPointer = f;
    					frontier.changePriority(w, (double) distance);
    				}
    			}
    		}
    	}
    	
        return new LinkedList<Node>(); // no path found
    }

    /** Return the path from the start node to node end.
     *  Precondition: nData contains all the necessary information about
     *  the path. */
    public static List<Node> constructPath(Node end, HashMap<Node, SFdata> nData) {
        LinkedList<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.addFirst(p);
            p= nData.get(p).backPointer;
        }
        return path;
    }
    
    /** Return the sum of the weights of the edges on path path. */
    public static int pathDistance(List<Node> path) {
        if (path.size() == 0) return 0;
        synchronized(path) {
            Iterator<Node> iter= path.iterator();
            Node p= iter.next();  // First node on path
            int s= 0;
            // invariant: s = sum of weights of edges from start to p
            while (iter.hasNext()) {
                Node q= iter.next();
                s= s + p.getEdge(q).length;
                p= q;
            }
            return s;
        }
    }
    
    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    private static class SFdata {
        private Node backPointer; // backpointer on path from start node to this one
        private int distance; // distance from start node to this one

        /** Constructor: an instance with distance d from the start node and
         *  backpointer p.*/
        private SFdata(int d, Node p) {
            distance= d;     // Distance from start node to this one.
            backPointer= p;  // Backpointer on the path (null if start node)
        }
        
        /** return a representation of this instance. */
        public String toString() {
            return "dist " + distance + ", bckptr " + backPointer;
        }
    }
}
