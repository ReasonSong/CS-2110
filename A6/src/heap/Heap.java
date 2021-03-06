package heap;
/* Time spent on a6: 02h hours and 42 minutes.

 * Name(s):Ruochen Song, Hongshu Ye
 * Netid(s):rs2352, hy483
 * What I thought about this assignment:
 *
 *
 *
 */

import java.lang.reflect.Array;

import java.util.*;

/** Please keep up with the pinned Piazza note A6 FAQs.
 * Study slides 9..49 of Lecture 17 on Priority Queues and Heaps
 * before starting on this assignment. */

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type double. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public class Heap<V> {

    /** The contents of c represent a complete binary tree. c[0] is the root;
     * c[2i+1] and c[2i+2] are the left and right children of c[i].
     * If i is not 0, c[(i-1)/2] (using integer division) is the parent of c[i].
     *
     * Class Invariant:
     *   1. The tree is complete. It consists of c[0..size-1].
     *
     *   2. For i in 0..size-1, c[i] contains the value and its priority.
     *      For i in size..c.length-1, c[i] = null.
     *
     *   3. The values in c[0..size-1] are all different
     *
     *   4. For i in 1..size-1, (c[i]'s priority) >= (c[i]'s parent's priority)
     *
     *   map and the tree are in sync:
     *     5. The keys of map are the values in c[0..size-1]
     *     6. c[map[v]] has value v
     *
     * Note that invariant 5 implies that this.size equals map.size
     */
    protected Entry[] c;
    protected int size;
    protected HashMap<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c= createEntryArray(10);
        map= new HashMap<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        V value;
        double priority;

        /** An Entry with value v and priority p*/
        Entry(V v, double p) {
            value= v;
            priority= p;
        }
    }

    /** Add v with priority p to the heap.
     *  Throw an illegalArgumentException if v is already in the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. */
    public void add(V v, double p) throws IllegalArgumentException {
        // TODO #1: Write this whole method. Note that bubbleUp is not implemented,
        // so calling it will have no effect (yet). The first tests of add, using
        // test00Add, ensure that this method maintains fields c and map properly,
        // without worrying about bubbling up. Look at the spec of test00Add.

        if(map.containsKey(v)) {
            throw new IllegalArgumentException();
        }
        ensureSpace();
        Entry entry = new Entry(v, p);
        c[size] = entry;
        map.put(v, size);
        bubbleUp(size);
        size++;
    }


    /** If size = length of c, double the length of array c. */
    public void ensureSpace() {
        //TODO #2. Any method that changes the size of array c needs to call
        // this method first. If you write this method correctly AND method
        // add calls this method appropriately, testing procedure
        // test10ensureSpace will not find errors.

        if(size == c.length) {
            c = Arrays.copyOf(c, c.length * 2);
        }
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return size;
    }

    /** Swap c[h] and c[k].
     *  Precondition: 0 <= h < c.size, 0 <= k < c.size. */
    void swap(int h, int k) {
        //TODO 3: When bubbling values up and down (later on), two values,
        // say c[h] and c[k], will have to be swapped. At the same time,
        // field map has to be maintained. In order to always get this right,
        // use method swap for this. Method swap is tested by testing
        // procedure test13Add_Swap --it will find no errors if you write
        // this method properly.

        if(0 <= h && h < c.length && 0 <= k && k < c.length) {
            Entry temp = new Entry(c[h].value, c[h].priority);
            c[h].value = c[k].value;
            c[h].priority = c[k].priority;
            map.put(c[h].value, h);
            c[k].value = temp.value;
            c[k].priority = temp.priority;
            map.put(c[k].value, k);
        }
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    private void bubbleUp(int k) {
        // TODO #4 As you know, this method should be called within add in order
        // to bubble a value up to its proper place, based on its priority.
        // Do not use recursion. Use iteration.
        // If this method is written properly, testing procedure
        // test15Add_BubbleUp() will not find any errors.

        int parentIndex = (k-1)/2;
        while((parentIndex >= 0) && c[k].priority < c[parentIndex].priority) {
            swap(k, parentIndex);
            k = parentIndex;
            parentIndex = (k-1)/2;
        }

    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  Throw a NoSuchElementException if the heap is empty. */
    public V peek() {
        // TODO 5: Do peek. This is an easy one.
        //         test20Peek() will not find errors if this is correct.

        if (size == 0){
            throw new NoSuchElementException();
        }
        return c[0].value;
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  Throw a NoSuchElementException if the heap is empty. */
    public V poll() {
        // TODO 6: Do poll and bubbleDown (#7) together. When they are
        //         written correctly, testing procedure test30Poll_BubbleDown_NoDups
        //         will not find errors.
        //
        //         Note also testing procedure test40testDuplicatePriorities
        //         This method tests to make sure that when bubbling up or down,
        //         two values with the same priority are not swapped.

        if (size == 0){
            throw new NoSuchElementException();
        }
        V v = c[0].value;
        swap(0, size-1);
        map.remove(v);
        size--;
        bubbleDown(0);
        return v;
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    private void bubbleDown(int k) {
        // TODO 7: Do poll (#6) and bubbleDown together. We also suggest
        //         implementing and using smallerChildOf, though you don't
        //         have to. Do not use recursion. Use iteration.
        int smallerChild = smallerChildOf(k);
        while ((size > 2 * k + 1) && c[k].priority > c[smallerChild].priority) {
            swap(k, smallerChild);
            k = smallerChild;
            smallerChild = smallerChildOf(k);
        }
    }

    /** Return the index of the smaller child of c[n]
     *  If the two children have the same priority, choose the right one.
     *  Precondition: left child exists: 2n+1 < size of heap */
     int smallerChildOf(int n) {
         int smallerChild = 2 * n + 2;
         if (smallerChild >= size
		 		|| c[smallerChild-1].priority < c[smallerChild].priority) {
             smallerChild = smallerChild - 1;
         }
         return smallerChild;
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  Throw an IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, double p) {
        // TODO  8: When this method is correctly implemented, testing procedure
        //          test50ChangePriority() won't find errors.

        if (map.containsKey(v) == false) {
            throw new IllegalArgumentException();
        }
        int index = map.get(v);
        double prevPriority = c[index].priority;
        c[index].priority = p;
        if(prevPriority < p) {
            bubbleDown(index);
        } else if (prevPriority > p) {
            bubbleUp(index);
        }
    }

    /** Create and return an Entry[] of size n.
     *  This is necessary because Generic and arrays don't interoperate nicely.
     *  A student in CS2110 would not be expected to know about the need
     *  for this method and how to write it. */
     Entry[] createEntryArray(int n) {
        return (Entry[]) Array.newInstance(Entry.class, n);
    }
}
