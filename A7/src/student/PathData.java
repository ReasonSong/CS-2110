package student;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import graph.Graph;
import graph.Node;

/** An instance contains the number of nodes in a graph, the 
 * names of the nodes, the nodes themselves, and a matrix of
 * shortest distances.  */
public  class PathData {
    
    // The ordering of the nodes in all list and in dist is the order 
    // given by their names in field names.
    public Graph graph;  // the graph
    public int size; // number of nodes
    public String[] names; // names of nodes
    public Node[] nodes;  // null if not calculated; otherwise, the nodes for names
    public int[][] dist; // Adjacency matrix, giving shortest distances

    /** Constructor: an instance for graph g with names n and distances d. */
    public PathData(String[] n, int[][] d, Graph g) {
        graph= g;
        size= n.length;
        names= n;
        dist= d;
        // calculate the nodes from g.
        nodes= new Node[size];
        for (int k= 0; k < size; k= k+1) {
            nodes[k]= g.getNode(names[k]);
        }
    }

    /** Constructor: an instance for graph G created from file s. */
    public PathData(String s, Graph g) {
        graph= g;
        try {
            FileReader fr= new FileReader(s);
            BufferedReader br= new BufferedReader(fr);
            readFile(br);
            br.close(); 
        } catch (IOException e) {

        }
        // calculate the nodes from g.
        nodes= new Node[size];
        for (int k= 0; k < size; k= k+1) {
            nodes[k]= g.getNode(names[k]);
        }
    }

    /** Read the size of the graph, the names of the nodes, and a
     * distance adjacency matrix from reader and place them in fields
     * size, names, and dist. */
    public void readFile(BufferedReader br) {
        try {
            String line= br.readLine().trim();
            size= Integer.parseInt(line);

            // Read in names of nodes (within double-quotes)
            // and populate field names
            names= new String[size];
            line= br.readLine().trim();
            for (int k= 0; k < size; k= k+1) {
                // get rid of first double quote
                int h= line.indexOf("\"");
                line= line.substring(h+1);
                
                h= line.indexOf("\"");
                names[k]= line.substring(0, h);
                
                line= line.substring(h+1).trim();
            }

            // Read in distances and populate field dist
            dist= new int[size][size];
            for (int row= 0; row < size; row= row + 1) {
                // Read and process line for row row
                line= br.readLine();
                for (int col= 0; col < size; col= col+1) {
                    int h= line.indexOf(' ');
                    if (h == -1) dist[row][col]= Integer.parseInt(line);
                    else { 
                        dist[row][col]= Integer.parseInt(line.substring(0, h));
                        line= line.substring(h+1);
                    }
                }
            } 
        } catch (IOException e) {
            throw new RuntimeException("IO error reading buffered reader " + br);
        }

    }
}