import io.TextIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import graph.Graph;

/** An instance represents a network of humans, used with a BugTree.
 * It is known that the names of all Humans's will be distinct --no duplicates. 
 * @author Mshnik */
public class Network extends Graph<Human, HumanConnection> {
    
    private int maxHealth; // The maximum health a human can have
    protected static String[] names; // Names of all human
    
    /** Read in the array of names from the names text file */
    static {
        try {
            names= TextIO.read(new File("data/names.txt"));
        } catch (IOException e) {
            System.err.println("Error reading names file, should be located at data/names.txt");
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /** Constructor: an instance with no humans and no connections. */
    public Network() {
        super();
    }

    /** Constructor: a graph of size humans of health mh with edges generated
     * randomly based on connectionProbability cp.
     * Preconditions: 0 <= size, 0 <= cp <= 1, 1 <= mh. */
    public Network(int size, double cp, int mh) {
        super();
        assert 0 <= size  &&  0 <= cp  &&  cp <= 1  &&  1 <= mh;
        maxHealth= mh;
        for (int i = 0; i < size; i++) {
            //Add itself to this as part of construction
            new Human(names[i], this, mh);
        }
        
        for (Human p1 : vertexSet()) {
            for (Human p2 : vertexSet()) {
                if (p1 != p2  && Math.random() < cp) {
                    addEdge(p1, p2, new HumanConnection());
                }
            }
        }
    }

    /** Constructor: an instance generated for the humans in dt.
     * There is an edge from each parent to each of its children. */
    public Network(BugTree dt) {
        super();
        addVertex(dt.getRoot());
        recCreate(dt);
    }

    /** Add to this Network the humans in children trees of dt,
     * adding edges from each root to its children.
     * Precondition: dt.getRoot is already in the graph. */
    private void recCreate(BugTree dt) {
        Human dtRoot= dt.getRoot();
        for (BugTree child : dt.copyOfChildren()){
            addVertex(child.getRoot());
            addEdge(dtRoot, child.getRoot(), new HumanConnection());
            recCreate(child);
        }
    }

    /** Return a list of humans in state s in this Network. */
    public List<Human> getHumansOfType(Human.State s) {
        ArrayList<Human> lst= new ArrayList<>();
        for (Human p : vertexSet()) {
            if (p.getState() == s) {
                lst.add(p);
            }
        }
        return lst;
    }

}
