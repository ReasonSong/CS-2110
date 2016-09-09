
/* NetId(s): djg17 mdg39 Time spent: hh hours, mm minutes.
 *
 * Name(s):
 * What I thought about this assignment:
 *
 *
 */

/** An instance is a doubly linked list.
 *
 * Note: Throughout, we refer to the values in this list as
 * this[0], this[1], this[2], etc.
 */
public class DLL<V> {
    private int size;  // number of values in the list.
    private Node head; // first node of the linked list (null if list is empty)
    private Node tail; // last  node of the linked list (null if list is empty)

    /** Constructor: an empty linked list. */
    public DLL() {
        // TODO item #1, along with #2 and #3
        // Write this constructor, function size(), and function toStringRev()
        // Then test using the testing procedure we gave you in the A3 handout.
        throw new UnsupportedOperationException();
    }

    /** = the number of values in this list.
     * This operation must take constant time. */
    public int size() {
        // TODO item #2, along with #1 and #3
        throw new UnsupportedOperationException();
    }

    /** Return a representation of this list: its values in order, with
     * adjacent ones separated by ", " (comma blank), with "[" at the beginning,
     * and with "]" at the end. <br/>
     * Values are turned into Strings using String catenation.
     * Thus, if a value is null, the String "null" is automatically used. */
    public @Override String toString() {
        String res= "[";
        // invariant: res = "[" + v1 + ", " + v2 + ", " + ... + ", " + vk
        //            where v1 ... vk are the values in all Nodes before n
        //            (or all if n is null)
        for (Node n= head; n != null; n= n.succ) {
            if (res.length() > 1)
                res= res + ", ";
            res= res + n.value;
        }
        return res + "]";
    }

    /** Return a representation of this list: its values in reverse order, with
     * adjacent ones separated by ", " (comma blank), with "[" at the beginning,
     * and with "]" at the end. <br/>
     *
     * E.g. for the list [6,3,8], the result is "[8, 3, 6]".
     * Values are turned into Strings using catenation, as in toString. */
    public String toStringRev() {
        // TODO item #3, along with #1 and #2.
        // Use field tail and the pred fields in elements. Do NOT use field size.
        String res= "[";
        // invariant: res = "[" + vn + ", " + v(k-1) + ", " + ... + ", " + vk
        //            where vn ... vk are the values in all Nodes after n
        //
        throw new UnsupportedOperationException();
    }

    /** Append val to the end of this list. */
    public void add(V val) {
        // TODO item #4
        // Write and test this carefully and completely. You probably want to be
        // sure that adding a first value works properly, adding a second value
        // works properly, adding a third value works properly, and adding a
        // fourth value works properly.
        throw new UnsupportedOperationException();
    }

    /** Return Node number h of the linked list (not the value, which is this[h]).
     * @throws IndexOutOfBoundsException if h < 0 or h >= size of the list.
     * This method must take time proportional to min(h, size - h). */
    /* package */ Node getNode(int h) {
        // TODO item #5, along with #6
        // This helper method is used a lot.
        // Test this method and function get(int) (#6 below) together.
        // That is, the main idea is to test get(int), but get(int) will call
        // this method so this method will be tested too.
        //
        // There are two ways to get to an element: from the head or from the tail.
        // This MUST use the fastest way for h.
        // (If h is exactly the middle, then either way is ok.)
        throw new UnsupportedOperationException();
    }

    /** Return this[h] --value number h of the list.
     * @throws IndexOutOfBoundsException if h < 0 or h >= size of the list.
     * The time taken should be proportional to min(h, size - h). */
    public V get(int h) {
        // TODO item #6, along with #5
        // Rely on function getNode(int) to keep this method VERY small.
        // No need to throw an exception here; getNode will do it.
        // This method must be tested completely; since it relies heavily
        // getNode(int), that method will be tested too.
        throw new UnsupportedOperationException();
    }

    /** Return value this[h] and replace it by val.
     * @throws IndexOutOfBoundsException if h < 0 or h >= size of the list.
     * The time taken should be proportional to min(h, size - h). */
    public V set(int h, V val) {
        // TODO item #7
        // Rely on function getNode(int) to keep this method small.
        // No need to throw an exception here; getNode will do it.
        throw new UnsupportedOperationException();
    }

    /** Insert val into a new Node before Node n of this list and return the new Node.
     * Precondition: n must be an Element of this list; it must not be null.
     * This operation must take constant time.  */
    /* package */ Node insertBefore(V val, Node n) {
        // TODO item #8, along with #9
        // This helper function will be used by add(int, V) below. Write both
        // of them before testing; tests that call add(int, V) will then test
        // both methods.
        //
        // Do NOT check whether n is actually a Node of this list because
        // it will then not be a constant-time operation.
        throw new UnsupportedOperationException();
    }

    /** Insert val as this[h]; thus, this[h..] becomes this[h+1..].
     * If h = size of the list, this means to append val to the list.
     * @throws IndexOutOfBoundsException if h < 0 or h > size of the list.
     * This operation must take time proportional to min(h, size - index). */
    public void add(int h, V val) {
        // TODO item #9, along with #8
        // Rely on three helper methods to keep this method small:
        // add(V), getNode(int), and insertBefore(V, int).
        // add and getNode will throw the exception; no need for it here.
        throw new UnsupportedOperationException();
    }

    /** Remove this[h] from the list and return the value that was removed.
     * @throws IndexOutOfBoundsException if h < 0 or h >= size of the list. */
    public V remove(int h) {
        // TODO item #9
        // In removing a Node from the linked list, be sure to take care of all cases!
        // Let a helper method throw the exception; don't do it here.
        //
        // It's good to set all fields of the removed node to null so that if a user
        // gets hold of the node no damage can be done.
        throw new UnsupportedOperationException();
    }

    ////////////////////////////////////////////////////////////////////////////

    /** An instance is a Node of this linked list. */
    /* package */ class Node {
        /** Predecessor of this Node on list (null if this is first Node). */
        /* package */ Node pred;

        /** The value in this element. */
        /* package */ V value;

        /** Successor of this Node on list. (null if this is last Node). */
        /* package */ Node succ;

        /** Constructor: an instance with predecessor node p (can be null),
          *  successor node s (can be null), and value val. */
        /* package */ Node(Node p, V val, Node s) {
            pred= p;
            succ= s;
            value= val;
        }
    }
}
