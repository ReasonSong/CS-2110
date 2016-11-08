package student;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import graph.Graph;
import graph.Node;
import gui.TextIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PathsTester {

    @Test
    /** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and no edges.
     * Test a 1-node path from Ithaca to Ithaca and
     * an empty shortest path from Ithaca to Truck Depot  */
    public void test10OneNodeBoard() {
        Graph g= getGraph("data/Maps/OneNodeBoard.txt");
        Node n= g.getNode("Ithaca");
        List<Node> path= Paths.shortestPath(n, n);
        List<Node> expected= new LinkedList<Node>();
        expected.add(n);
        assertEquals(expected, path);
    }

    @Test
    /** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and no edges.
     * Test a 1-node path from Ithaca to Ithaca and
     * an empty shortest path from Ithaca to Truck Depot  */
    public void test20NoEdges() {
        Graph g= getGraph("data/Maps/TwoNodeNoEdge.txt");
        Node n= g.getNode("Ithaca");
        List<Node> path= Paths.shortestPath(n, n);
        List<Node> expected= new LinkedList<Node>();
        expected.add(n);
        assertEquals(expected, path);

        Node n1= g.getNode("Truck Depot");
        List<Node> path1= Paths.shortestPath(n, n1);
        List<Node> expected1= new LinkedList<Node>();
        assertEquals(expected1, path1);
    }

    @Test
    /** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and
     * an edge between them.
     * Test a 1-node path from Ithaca to Ithaca,
     * a 2-node shortest path from Ithaca to Truck Depot, and
     * a 2-node shortest path from Ithaca to Truck Depot  */
    public void test30TwoNodeOneEdge() {
        Graph g= getGraph("data/Maps/TwoNodeBoard.txt");
        Node n= g.getNode("Ithaca");
        List<Node> path= Paths.shortestPath(n, n);
        List<Node> expected= new LinkedList<Node>();
        expected.add(n);
        assertEquals(expected, path);

        Node n1= g.getNode("Truck Depot");
        List<Node> path1= Paths.shortestPath(n, n1);
        List<Node> expected1= new LinkedList<Node>();
        expected1.add(n);
        expected1.add(n1);
        System.out.println("Expected is: " + expected1);
        System.out.println("calculated is: " + path1);
        assertEquals(expected1, path1);

        List<Node> path2= Paths.shortestPath(n1, n);
        List<Node> expected2= new LinkedList<Node>();
        expected2.add(n1);
        expected2.add(n);
        assertEquals(expected2, path2);
    }

    @Test
    /** Test all shortest paths on map TestBoard1.txt  */
    public void test40MapTestBoard1() {
        Graph g= getGraph("data/Maps/TestBoard1.txt");
        PathData pd= new PathData("data/Maps/TestBoard1distances.txt", g);
        assertEquals(3, pd.size);
        checkAllShortestPaths(g, pd);
    }

    @Test
    /** Test all shortest paths on map seed16.txt  */
    public void test50MapSeed16() {
        Graph g= getGraph("data/Maps/Seed16.txt");
        PathData pd= new PathData("data/Maps/Seed16distances.txt", g);
        assertEquals(6, pd.size);
        checkAllShortestPaths(g, pd);
    }

    @Test
    /** Test all shortest paths on map ...t  */
    public void test60MapBoard3() {
        Graph g= getGraph("data/Maps/Board3.txt");
        PathData pd= new PathData("data/Maps/Board3distances.txt", g);
        assertEquals(10, pd.size);
        checkAllShortestPaths(g, pd);
    }
    
    @Test
    /** Test all shortest paths on map ...t  */
    public void test70MapTestBoard2() {
        Graph g= getGraph("data/Maps/TestBoard2.txt");
        PathData pd= new PathData("data/Maps/TestBoard2distances.txt", g);
        assertEquals(34, pd.size);
        checkAllShortestPaths(g, pd);
    }
    
    @Test
    /** Test all shortest paths on map seededw with  */
    public void test80MapSeeded() {
        Graph g= Graph.randomBoard(5842236091853851505L);
        PathData pd= new PathData("data/Maps/Seed5842236091853851505L.txt", g);
        assertEquals(46, pd.size);
        checkAllShortestPaths(g, pd);
    }

    /** Check the shortest paths in g from each node to each node, as
     * given by Paths.shortestPaths, matches that in pd. Use the ordering
     * of nodes as give in pd.names. */
    public void checkAllShortestPaths(Graph g, PathData pd) {
        for (int r= 0; r < pd.size; r= r + 1) {
            for (int c= 0; c < pd.size; c= c + 1 ) {
                // check shortest path distance from node r to node c
                List list= Paths.shortestPath(pd.nodes[r], pd.nodes[c]);
                assertEquals(pd.dist[r][c], Paths.pathDistance(list));

                // check that first node of path and last node of path are correct
                if (list.size() > 0) {
                    assertEquals(pd.nodes[r], list.get(0));
                    assertEquals(pd.nodes[c], list.get(list.size()-1));
                }
            }
        }

    }

    /** Return a graph for file named s in the data. */
    public Graph getGraph(String s) {
        try {
            return Graph.getJsonGraph(new JSONObject(TextIO.read(new File(s))));
        } catch (IOException e) {
            throw new RuntimeException("IO Exception reading in graph " + s);
        }
    }
}


