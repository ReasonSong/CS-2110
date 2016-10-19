/* NetId(s): rs2352, hy483. Time spent: 04 hours, 15 minutes.

 * Name(s): Ruochen Song, Hongshu Ye
 * What I thought about this assignment:
 * 
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** An instance of BugTree represents the spreading of a bug among humans.
 * Each human may "catch" the bug from only other human.
 * The root of the BugTree is the human who first got the bug. Each human in the
 * BugTree is the child of the human who gave them the bug. For example, for the tree:
 * <p>
 *       A
 *      / \
 *     B   C
 *        / \
 *       D   E
 * <p>
 * Human A originally got the bug, B and C caught the bug from A,
 * and D and E caught the bug from C.
 * <p>
 * Important note: The name of each human in the bug tree is unique.
 *
 * @author Mshnik
 */
public class BugTree {

    /** The String to be used as a separator in toString() */
    public static final String SEPARATOR= " - ";

    /**  The String that marks the start of children in toString() */
    public static final String START_CHILDREN_DELIMITER= "[";

    /** The String that divides children in toString()  */
    public static final String DELIMITER= ", ";

    /** The String that marks the end of children in toString() */
    public static final String END_CHILDREN_DELIMITER = "]";

    /** The String that is the space increment in toStringVerbose() */
    public static final String VERBOSE_SPACE_INCREMENT= "\t";

    /** The human at the root of this BugTree.
     * This is the bug forebear of everyone in this BugTree: the
     * human who got infected first and indirectly caused everyone in it to get
     * infected. root is non-null.
     * All Human's in a BugTree have different names. There are no duplicates
     */
    private Human root;

    /** The children of this BugTree node.
     * Each element of children got the bug from the human at this node.
     * root is non-null but will be an empty set if this is a leaf.  */
    private Set<BugTree> children;

    /** Constructor: a new BugTree with root p and no children.
     * Throw an IllegalArgumentException if p is null. */
    public BugTree(Human p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException("Can't construct BugTree with null root");
        root= p;
        children= new HashSet<>();
    }

    /** Constructor: a new BugTree that is a copy of tree p.
     * Tree p and its copy have no node in common (but nodes can share a Human).
     * Throw an IllegalArgumentException if p is null.  */
    public BugTree(BugTree p) throws IllegalArgumentException {
        if (p == null)
            throw new IllegalArgumentException("Can't construct copy of null BugTree");
        root= p.root;
        children= new HashSet<>();

        for (BugTree dt : p.children) {
            children.add(new BugTree(dt));
        }
    }

    /** Return the human that is at the root of this BugTree. */
    public Human getRoot() {
        return root;
    }

    /** Return the number of direct children of this BugTree. */
    public int numberOfChildren() {
        return children.size();
    }

    /** Return a COPY of the set of children of this BugTree.  */
    public Set<BugTree> copyOfChildren() {
        return new HashSet<>(children);
    }

    /** Return the BugTree object in this tree whose root is  p
     * (null if p is not in this tree).
     * <p>
     * Example: Calling getTree(root) will return this. */
    public BugTree getTree(Human p) {
        if (root == p) return this; //Base case - look here

        // Recursive case - ask children to look
        for (BugTree dt : children) {
            BugTree search= dt.getTree(p);
            if (search != null) return search;
        }

        return null; // p is not in the tree
    }

    /** Add c to this BugTree as a child of p and
     * return the BugTree whose root is the new child.
     * Throw an IllegalArgumentException if:<br>
     * -- p or c is null,<br>
     * -- c is already in this BugTree, or<br>
     * -- p is not in this BugTree
     * There is a simple, non-recursive implementation of this function */
    public BugTree add(Human p, Human c) throws IllegalArgumentException {
        //TODO 1
        // Note: Do not test for p being in the BugTree by calling contains
        // because that may end up doing extra work. This method requires you
        // to find the BugTree whose root is p. Look in this class for an
        // already written method to do this. */
        
        if (p == null ||  c == null)
            throw new IllegalArgumentException("Can't add a null child to a null node.");
        if (getTree(c) != null)
            throw new IllegalArgumentException("Child already exist in this BugTree.");
        if (getTree(p) == null)
            throw new IllegalArgumentException("Child can't be added to a node doesn't exist.");
        
        BugTree newTree = new BugTree(c);
        getTree(p).children.add(newTree);
        
        return newTree;
    }

    /** Return the number of humans in this BugTree.
     * Note: If this is a leaf, the size is 1 (just the root) */
    public int size() {
        //TODO 2
        
        if (children.size() == 0) return 1;
        
        int size = 1;
        for (BugTree dt : children) {
            size += dt.size();
        }
        
        return size;
    }

    /*** Return the depth at which p occurs in this BugTree, or -1
     * if p is not in the BugTree.
     * Note: depthOf(root) is 0.
     * If p is a child of this BugTree, then depth(p) is 1. etc. */
    public int depthOf(Human p) {
        //TODO 3
        // Note: Do NOT call function contains(p) to find out whether p is in
        // the tree. The recursive case consists of looking for the depth of p
        // in each child. If looking in one child finds the depth, just return
        // the answer, thus terminating execution of the method.
        // If checking each child recursively doesn't find that p is in the tree,
        // return -1 at the end of the method.
        
        if (p == root) return 0;
        if (p == null || children.size() == 0) return -1;   // no children
        if (children.contains(p)) return 1;     // p found
        
        for (BugTree dt : children) {
            int depth = dt.depthOf(p);
            if(depth != -1) return depth + 1;   // p found in this child
        }
        
        // p is not found in any child
        return -1;
    }

    /** Return true iff this BugTree contains p. */
    public boolean contains(Human p) {
        //TODO 4
        /* Note: This BugTree contains p iff the root of this BugTree is
         * p or if one of p's children contains p. */
        
        if (depthOf(p) >= 0) return true;
    
        return false; // Replace this by the correct code
    }

    /** Return the maximum depth of this BugTree, i.e. the longest path from
     * the root to a leaf. Example. If this BugTree is a leaf, return 0. */
    public int maxDepth() {
        int maxDepth= 0;
        for (BugTree dt : children) {
            maxDepth = Math.max(maxDepth, dt.maxDepth() + 1);
        }
        return maxDepth;
    }

    /** Return the width of this tree at depth d (i.e. the number of BugTrees
     * that occur at depth d, where the depth of the root is 0.
     * Throw an IllegalArgumentException if depth < 0.
     * Thus, for the following tree :
     * Depth level:
     * 0       A
     *        / \
     * 1     B   C
     *      /   / \
     * 2   D   E   F
     *          \
     * 3         G
     * <p>
     * A.widthAtDepth(0) = 1,  A.widthAtDepth(1) = 2,
     * A.widthAtDepth(2) = 3,  A.widthAtDepth(3) = 1,
     * A.widthAtDepth(4) = 0.
     * C.widthAtDepth(0) = 1,   C.widthAtDepth(1) = 2
     */
    public int widthAtDepth(int d) throws IllegalArgumentException {
        //TODO 5
        // Hint: Use this recursive definition. If d = 0, the answer is 1.
        // If d > 0, the answer is: sum of widths of the children at depth d-1.
        if (d < 0)
            throw new IllegalArgumentException("Depth can't be negative.");
        
        if (d > maxDepth()) return 0;
        if (d == 0) return 1;
        
        int width = 0;
        for (BugTree dt : children) {
            width += dt.widthAtDepth(d - 1);
        }
        
        return width;
    }

    /** Return the maximum width of all the widths in this tree, i.e. the
     * maximum value that could be returned from widthAtDepth for this tree. */
    public int maxWidth() {
        return maxWidthImplementationTwo(this);
    }

    /** Simple implementation of maxWidth. Relies on widthAtDepth.
     * Takes time proportional to the square of the size of the t. */
    static int maxWidthImplementationOne(BugTree t) {
        int width= 0;
        int depth= t.maxDepth();
        for (int i= 0; i <= depth; i++) {
            width= Math.max(width, t.widthAtDepth(i));
        }
        return width;
    }

    /** Better implementation of maxWidth. Caches results in an array.
     * Takes time proportional to the size of t. */
    static int maxWidthImplementationTwo(BugTree t) {
        // For each integer d, 0 <= d <= maximum depth of t, store in
        // widths[d] the number of nodes at depth d in t.
        // The calculation is done by calling recursive procedure addToWidths.
        int[] widths= new int[t.maxDepth() + 1];   // initially, contains 0's
        t.addToWidths(0, widths);

        int max= 0;
        for (int width : widths) {
            max= Math.max(max, width);
        }
        return max;
    }

    /** For each node of this BugTree, which is at some depth d in this
     * BugTree, add 1 to widths[depth + d]. */
    private void addToWidths(int depth, int[] widths) {
        widths[depth]++;        //the root of this BugTree is at depth d = 0
        for (BugTree dt : children) {
            dt.addToWidths(depth + 1, widths);
        }
    }

    /** Better implementation of maxWidth. Caches results in a HashMap.
     * Takes time proportional to the size of t. */
    static int maxWidthImplementationThree(BugTree t) {
        // For each possible depth d >= 0 in tree t, widthMap will contain the
        // entry (d, number of nodes at depth d in t). The calculation is
        // done using recursive procedure addToWidthMap.

        // For each integer d, 0 <= d <= maximum depth of t, add to
        // widthMap an entry <d, 0>.
        HashMap<Integer, Integer> widthMap= new HashMap<>();
        for (int d= 0; d <= t.maxDepth() + 1; d++) {
            widthMap.put(d, 0);
        }

        t.addToWidthMap(0, widthMap);

        int max= 0;
        for (Integer w : widthMap.values()) {
            max= Math.max(max, w);
        }
        return max;
    }

    /** For each node of this BugTree, which is at some depth d in this BugTree,
     * add 1 to the value part of entry <depth + d, ...> of widthMap. */
    private void addToWidthMap(int depth, HashMap<Integer, Integer> widthMap) {
        widthMap.put(depth, widthMap.get(depth) + 1);  //the root is at depth d = 0
        for (BugTree dt : children) {
            dt.addToWidthMap(depth + 1, widthMap);
        }
    }

    /** Return the route the bug took to get from "here" (the root of this
     * BugTree) to child c. For example, for this tree:
     * <p>
     * Depth level:
     * 0        A
     *         / \
     * 1      B   C
     *       /   / \
     * 2    D   E   F
     *       \
     * 3      G
     * <p>
     * A.bugRouteTo(E) should return a list of [A,C,E].
     * A.bugRouteTo(A) should return [A].
     * A.bugRouteTo(X) should return null.
     * <p>
     * B.bugRouteTo(C) should return null.
     * B.bugRouteTo(D) should return [B,D] */
    public List<Human> bugRouteTo(Human c) {
        //TODO 6
        // Note: You have to return a List<Human>. But List is an
        // interface, so use something that implements it.
        // LinkedList<Human> is preferred to ArrayList<Human>, because
        // prepend (or its equivalent) may have to be used.
        // Base Case: The root of this BugTree is c. Route is just [c].
        
        if (!contains(c)) return null;
        
        LinkedList<Human> route = new LinkedList<Human>();
        BugTree temp = this;
        while (temp.getRoot() != c){
            route.add(temp.getRoot());
            for (BugTree dt : temp.children) {
                if (dt.contains(c)) {
                    temp = dt;
                    break; 
                }
            }
        }
        route.add(temp.getRoot());      
        return route;
    }

    /** Return the immediate parent of c (null if c is not in this BugTree).
     * <p>
     * Thus, for the following tree:
     * Depth level:
     * 0      A
     *       / \
     * 1    B   C
     *     /   / \
     * 2  D   E   F
     *     \
     * 3    G
     * <p>
     * A.getParent(E) returns C.
     * C.getParent(E) returns C.
     * A.getParent(B) returns A.
     * E.getParent(F) returns null.
     */
    public Human getParent(Human c) {
        // Base case
        for (BugTree dt : children) {
            if (dt.root == c) return root;
        }

        // Recursive case - ask children to look
        for (BugTree dt : children) {
            Human parent= dt.getParent(c);
            if (parent != null) return parent;
        }

        return null; //Not found
    }

    /** If either child1 or child2 is null or is not in this BugTree, return null.
     * Otherwise, return the human at the root of the smallest subtree of this
     * BugTree that contains child1 and child2.
     * <p>
     * Examples. For the following tree (which does not contain H):
     * <p>
     * Depth level:
     * 0      A
     *       / \
     * 1    B   C
     *     /   / \
     * 2  D   E   F
     *     \
     * 3    G
     * <p>
     * A.sharedForebearOf(B, A) is A
     * A.sharedForebearOf(B, B) is B
     * A.sharedForebearOf(B, C) is A
     * A.sharedForebearOf(A, C) is A
     * A.sharedForebearOf(E, F) is C
     * A.sharedForebearOf(G, F) is A
     * B.sharedForebearOf(B, E) is null
     * B.sharedForebearOf(B, A) is null
     * B.sharedForebearOf(D, F) is null
     * B.sharedForebearOf(D, H) is null
     * A.sharedForebearOf(null, C) is null
     */
    public Human sharedForebearOf(Human child1, Human child2) {
        //TODO 7
        // HINT: if you are smart about this, you will realize that recursion
        // is not needed if you make use of an already written method
        
        if (child1 == null || child2 == null 
                || !contains(child1) || !contains(child2)) return null;
        
        List<Human> route1 = bugRouteTo(child1);
        List<Human> route2 = bugRouteTo(child2);
        int depth = 0;
        int routeDepth = Math.min(route1.size(), route2.size());
        while(route1.get(depth) == route2.get(depth) && depth < routeDepth - 1)
            ++ depth;
        if (route1.get(depth) != route2.get(depth)) -- depth;
        
        return route1.get(depth);
    }

    /** Return a (single line) String representation of this BugTree.
     * If this BugTree has no children (it is a leaf), return the root's substring.
     * Otherwise, return
     * root's substring + SEPARATOR + START_CHILDREN_DELIMITER + each child's
     * toString, separated by DELIMITER, followed by END_CHILD_DELIMITER.
     * Make sure there is not an extra DELIMITER following the last child.
     * <p>
     * Finally, make sure to use the static final fields declared at the top of
     * BugTree.java.
     * <p>
     * Thus, for the following tree:
     * Depth level:
     * 0      A
     *       / \
     * 1     B  C
     *      /  / \
     * 2   D  E   F
     *      \
     * 3     G
     * A.toString() should print:
     * (A) - HEALTHY - [(C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]], (B) - HEALTHY - [(D) - HEALTHY]]
     * <p>
     * C.toString() should print:
     * (C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]]
     */
    public String toString() {
        if (children.isEmpty()) return root.toString();
        String s= root.toString() + SEPARATOR + START_CHILDREN_DELIMITER;
        for (BugTree dt : children) {
            s= s + dt.toString() + DELIMITER;
        }
        return s.substring(0, s.length() - 2) + END_CHILDREN_DELIMITER;
    }


    /** Return a verbose (multi-line) string representing this BugTree. */
    public String toStringVerbose() {
        return toStringVerbose(0);
    }

    /** Return a verbose (multi-line) string representing this BugTree.
     * Each human in the tree is on its own line, with indentation representing
     * what each human is a child of.
     * indent is the the amount of indentation to put before this line.
     * Should increase on recursive calls to children to create the above pattern.
     * Thus, for the following tree:
     * Depth level:
     * 0      A
     *       / \
     * 1    B   C
     *     /   / \
     * 2  D   E   F
     *     \
     * 3    G
     * <p>
     * A.toStringVerbose(0) should return:
     * (A) - HEALTHY
     * (C) - HEALTHY
     * (F) - HEALTHY
     * (E) - HEALTHY
     * (G) - HEALTHY
     * (B) - HEALTHY
     * (D) - HEALTHY
     * <p>
     * Make sure to use VERBOSE_SPACE_INCREMENT for indentation.
     */
    private String toStringVerbose(int indent) {
        String s= "";
        for (int i= 0; i < indent; i++) {
            s= s + VERBOSE_SPACE_INCREMENT;
        }
        s= s + root.toString();

        if (children.isEmpty()) return s;

        for (BugTree dt : children) {
            s= s + "\n" + dt.toStringVerbose(indent + 1);
        }
        return s;
    }

    /** Return true iff this is equal to ob.
     * If ob is not a BugTree, it cannot be equal to this BugTree, so return false.
     * Two BugTree are equal if they are the same object (==) or:
     * <br> - they have the same root Human object (==)
     * <br> - their children sets are equal, which requires:
     * <br> --- the two sets are of the same size
     * <br> --- for every BugTree dt in one set, there is a BugTree dt2
     * in the other set for which dt.equals(dt2) is true.
     * <p>
     * Otherwise the two BugTree are not equal.
     * Do not use any of the toString functions to write equals().
     * You can write a helper function --we found it useful-- but if you do,
     * put a precise specification on it.
     */
    public boolean equals(Object ob) {
        //TODO 8
        // Hint about checking whether each child of one tree equals SOME
        // other tree of the other tree's children.
        // First, you have to check them all until you find an equal one (or
        // return false if you don't.)
        // Second, you know that a child of one tree cannot equal more than one
        // child of another tree because the names of Human's are all unique;
        // there are no duplicates.
        
        if (ob == null || !(ob instanceof BugTree)) return false;
        if (ob == this) return true;
        
        BugTree tree = (BugTree)ob;
        if (tree.root != this.root) return false;
        
        boolean equality = true;
        if (tree.children.size() == this.children.size() && tree.children != null || this.children != null) {
            for (BugTree bt : this.children){
                equality = equality && childrenEquals(bt, tree.children);
            }
        }
        
        return equality;
    }
    
    // This helper function is to check if the children of this BugTree and object are the same
    private boolean childrenEquals(BugTree bt, Set<BugTree> b2) {
        for (BugTree bt2 : b2) {
            if (bt.equals(bt2)) return true;
        }
        return false;       
    }
    
}
