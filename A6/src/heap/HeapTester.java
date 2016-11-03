package heap;

//import static com.autograder.shared.FunctionalAssert.assertEquals;
//import static com.autograder.shared.FunctionalAssert.assertNoError;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import heap.Heap.Entry;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HeapTester {

    /** Use assertEquals to check that all fields of mh are correct.
     *  This means that:
     *    (1) b.length, mh.size, and mh.map.size() are all equal.
     *    (2) for each i in 0..size-1, (b[i], p[i]) is the entry mh.c[i]
     *    (3) For each i in 0..size-1, (b[i], i) is in map.
     *    
     *    Precondition: b.length = p.length.
     *                  No two priorities in p differ by less than .0001
     *                  (b, p) is actually a good heap.
     *   */
    public <T> void check(T[] b, double p[], Heap<T> mh) {
        assert b.length == p.length;
        // check sizes
        assertEquals(b.length, mh.size);
        assertEquals(b.length, mh.map.size());

        // check values
        String stringB= toStringB(b);
        String stringC= toStringHeapValues(mh);
        assertEquals(stringB, stringC);

        // check priorities
        String stringBpriorities= toStringB(p);
        String stringCpriorities= toStringHeapPriorities(mh);
        assertEquals(stringBpriorities, stringCpriorities);

        // check maps
        ArrayList<Integer> s= new ArrayList<Integer>();
        for (int k= 0; k < b.length; k= k+1) {s.add(k);}
        ArrayList<Integer> mhS= new ArrayList<Integer>();
        for (int k= 0; k < b.length; k= k+1) {mhS.add(mh.map.get(b[k]));}
        assertEquals(s, mhS);
    }

    /** Use assertEquals to check that expected value m1 and 
     * computed value m2 are equal. */
    public void check(Heap<Integer> m1, Heap<Integer> m2) {
        // check sizes
        assertEquals(m1.size, m2.size);
        assertEquals(m1.size, m2.map.size());

        // check values
        String stringM1= toStringHeapValues(m1);
        String stringM2= toStringHeapValues(m2);
        assertEquals(stringM1, stringM2);

        // check priorities
        String stringM1p= toStringHeapPriorities(m1);
        String stringM2p= toStringHeapPriorities(m2);
        assertEquals(stringM1p, stringM2p);

        //check maps
        assertTrue(m1.map.equals(m2.map));
    }

    /** = a list of values in b, separated by ", " and delimited by "[", "]" */
    public <V> String toStringB(V[] b) {
        String res= "[";
        for (int h= 0; h < b.length; h= h+1) {
            if (h > 0) res= res + ", ";
            res= res + b[h];
        }
        return res + "]";
    }

    /** = a list of values in b, separated by ", " and delimited by "[", "]" */
    public String toStringB(double[] b) {
        String res= "[";
        for (int h= 0; h < b.length; h= h+1) {
            if (h > 0) res= res + ", ";
            res= res + b[h];
        }
        return res + "]";
    }

    /** = a list of values in mh.c[0..mh.size-1], separated by ", " and delimited by "[", "]" */
    public <V> String toStringHeapValues(Heap<V> mh) {
        String res= "[";
        for (int h= 0; h < mh.size; h= h+1) {
            if (h > 0) res= res + ", ";
            res= res + mh.c[h].value;
        }
        return res + "]";
    }

    /** = a list of priorities in mh.c[0..mh.size-1], separated by ", " and delimited by "[", "]" */
    public <V> String toStringHeapPriorities(Heap<V> mh) {
        String res= "[";
        for (int h= 0; h < mh.size; h= h+1) {
            if (h > 0) res= res + ", ";
            res= res + mh.c[h].priority;
        }
        return res + "]";
    }


    /** Return a heap with the values of b added to it, in that order. The 
     * priorities are the values. */
    public Heap<Integer> heapify(Integer[] b) {
        Heap<Integer> m= new Heap<Integer>();
        for (Integer e : b) {
            m.add(e, (double)e);
        }
        return m;
    }

    /** Return a heap with the values of b and corresponding priorities p
     * added to it, in that order.  */
    public Heap<Integer> heapify(Integer[] b, double[] p) {
        Heap<Integer> m= new Heap<Integer>();
        for (int h= 0; h < b.length; h= h+1) {
            int h1= h;
            m.add(b[h1], p[h1]);
        }
        return m;
    }

    /** Return a heap with the values of b and corresponding priorities p
     * added to it, in that order. */
    public Heap<String> heapify(String[] b, double[] p) {
        Heap<String> m= new Heap<String>();
        for (int h= 0; h < b.length; h= h+1) {
            m.add(b[h], p[h]);
        }
        return m;
    }

    /** Return a heap with values b without using add. Values used as priorities */
    public Heap<Integer> griesHeapify(Integer[] b) {
        Heap<Integer> heap= new Heap<Integer>();
        heap.c= heap.createEntryArray(b.length);
        for (int k= 0; k < b.length; k= k+1 ) {
            double bk= b[k];
            heap.c[k]= heap.new Entry(b[k], bk);
            heap.map.put(b[k], k);
        }
        heap.size= b.length;
        return heap;
    }

    /** Return a heap with values b and priorities p without using add. */
    public Heap<Integer> griesHeapify(Integer[] b, double[] p) {
        Heap<Integer> heap= new Heap<Integer>();
        heap.c= heap.createEntryArray(b.length); //new Entry[b.length];
        for (int k= 0; k < b.length; k= k+1 ) {
            heap.c[k]= heap.new Entry(b[k], p[k]);
            heap.map.put(b[k], k);
        }
        heap.size= b.length;
        return heap;
    }

    @Test
    /** Test whether add works when the priority of the value being added is
     * not smaller than priorities of other values in the heap. To test, we add
     * 3 values and then test. */
    public void test00Add() {
        Heap<Integer> mh= heapify(new Integer[] {5, 7, 8});
        check(new Integer[]{5, 7, 8}, new double[]{5.0, 7.0, 8.0}, mh);
    }

    @Test
    /** Test that add throws the exception properly. */
    public void test01AddException() {
        Heap<Integer> mh2= heapify(new Integer[] {5, 7, 8});
        try {
            mh2.add(5, 5.0);
            fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than IllegalArgumentException");
        }
    }


    @Test
    /** Test that adding integers 0..59 to a heap, with priorities same as values,
     *  works. This will test that ensureSpace works 3 times.
     *  Since the initial capacity of the heap is 10, it should be 80 at end. */
    public void test10ensureSpace() {
        Heap<Integer> mh= new Heap<Integer>();
        Integer[] b= new Integer[60];
        double[] p= new double[60];
        for (int k= 0; k < 60; k= k+1) {
            int k1= k;
            mh.add(k1, (double)k1);
            b[k]= k;
            p[k]= (double)k;
        }
        check(b, p, mh);
        assertEquals(80, mh.c.length);
    }

    @Test
    /** Test Heap.swap. This is done using the fact that if the priority of
     *  a value being added is >= priorities of values in the heap, the added
     *  value is placed at the end of the heap. */
    public void test13Add_Swap() {
        Heap<Integer> mh= heapify(new Integer[] {5, 7, 8, 9});
        mh.swap(0, 1); // should be {7, 5, 8, 9}
        mh.swap(1, 2); // should be {7, 8, 5, 9}
        mh.swap(0, 3); // should be {9, 8, 5, 7}
        mh.swap(2, 2); // should be {9, 8, 5, 7}
        check(new Integer[]{9, 8, 5, 7}, new double[]{9.0, 8.0, 5.0, 7.0}, mh);
    }

    @Test
    /** Test add and bubble up. */
    public void test15Add_BubbleUp() {
        Heap<Integer> mh= griesHeapify(new Integer[]{3, 6, 8});
        String msg= "Adding 5 to heap [3, 6, 8]";
        mh.add(5, 5.0);
        check(new Integer[]{3, 5, 8, 6}, new double[]{3.0, 5.0, 8.0, 6.0}, mh);

        Heap<Integer> mh1= griesHeapify(new Integer[]{3, 5, 8, 6});
        String msg1= "Adding 4 to heap [3, 5, 6, 8]";
        mh1.add(4, 4.0);
        check(new Integer[]{3, 4, 8, 6, 5}, new double[]{3.0, 4.0, 8.0, 6.0, 5.0}, mh1);

        Heap<Integer> mh2= griesHeapify(new Integer[]{3, 6, 8});
        mh2.add(5, 5.0);
        check(new Integer[]{3, 5, 8, 6}, new double[]{3.0, 5.0, 8.0, 6.0}, mh2);

        Heap<Integer> mh3= griesHeapify(new Integer[]{3, 5, 6, 8});
        mh3.add(4, 4.0);
        check(new Integer[]{3, 4, 6, 8, 5}, new double[]{3.0, 4.0, 6.0, 8.0, 5.0}, mh3);

        Heap<Integer> mh4= griesHeapify(new Integer[]{3, 4, 8, 6, 5});
        String msg4= "Adding 1 to heap [3, 4, 8, 6, 5]";
        mh4.add(1, 1.0);
        check(new Integer[]{1, 4, 3, 6, 5, 8}, new double[]{1.0, 4.0, 3.0, 6.0, 5.0, 8.0}, mh4);
    }

    @Test
    /** Test add and bubble up with duplicate priorities */
    public void test17Add_BubbleUpDuplicatePriorities() {
        Heap<Integer> mh= griesHeapify(new Integer[]{4});
        String msg= "Adding (2, 4.0) to heap []";
        mh.add(2, 4.0);
        check(new Integer[]{4, 2}, new double[]{4.0, 4.0}, mh);

        Heap<Integer> mh1= griesHeapify(new Integer[]{4, 2}, new double[] {4.0, 4.0});
        String msg1= "Adding (1, 4.0) to heap [4,2] --all priorities 4.0";
        mh1.add(1, 4.0);
        check(new Integer[]{4, 2, 1}, new double[]{4.0, 4.0, 4.0}, mh1);

        Heap<Integer> mh2= griesHeapify(new Integer[]{4, 2, 1}, new double[] {4.0, 4.0, 4.0});
        String msg2= "Adding (0, 4.0) to heap [4, 2, 1] --all priorities 4.0";
        mh2.add(0, 4.0);
        check(new Integer[]{4, 2, 1, 0}, new double[]{4.0, 4.0, 4.0, 4.0}, mh2);
    }

    @Test
    /** Test peek. */
    public void test20Peek() {
        Heap<Integer> mh= griesHeapify(new Integer[]{1, 3});
        String msg= "Testing peek on heep [1, 3] --values are priorities";
        assertEquals(new Integer(1), mh.peek());
        check(new Integer[]{1, 3}, new double[]{1.0, 3.0}, mh);

        Heap<Integer> mh1= griesHeapify(new Integer[] {});
        try {
            mh1.peek();  fail("Didn't throw an exception");
        } catch (NoSuchElementException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than IllegalArgumentException");
        }
    }

    @Test
    /** Test poll and bubbledown with no duplicate priorities. */
    public void test30Poll_BubbleDown_NoDups() {
        Heap<Integer> mh= heapify(new Integer[]{5});
        Integer res= mh.poll();
        assertEquals(new Integer(5), res);
        check(new Integer[]{}, new double[]{}, mh);

        Heap<Integer> mh1= heapify(new Integer[]{5, 6});
        Integer res1= mh1.poll();
        assertEquals(new Integer(5), res1);
        check(new Integer[]{6}, new double[]{6.0}, mh1);

        // this requires comparing lchild and rchild and using lchild
        Heap<Integer> mh2= heapify(new Integer[] {4, 5, 6, 7, 8, 9});
        Integer res2= mh2.poll();
        assertEquals(new Integer(4), res2);
        check(new Integer[]{5, 7, 6, 9, 8}, new double[]{5.0, 7.0, 6.0, 9.0, 8.0}, mh2);

        // this requires comparing lchild and rchild and using rchild
        Heap<Integer> mh3= heapify(new Integer[] {4, 6, 5, 7, 8, 9});
        Integer res3= mh3.poll();
        assertEquals(new Integer(4), res3);
        check(new Integer[]{5, 6, 9, 7, 8}, new double[]{5.0, 6.0, 9.0, 7.0, 8.0}, mh3);

        // this requires bubbling down when only one child
        Heap<Integer> mh4= heapify(new Integer[] {4, 5, 6, 7, 8});
        Integer res4= mh4.poll();
        assertEquals(new Integer(4), res4);
        check(new Integer[]{5,7, 6, 8}, new double[]{5.0, 7.0, 6.0, 8.0}, mh4);

        Heap<Integer> mh5= heapify(new Integer[] {2, 4, 3, 6, 7, 8, 9});
        Integer res5= mh5.poll();
        assertEquals(new Integer(2), res5);
        check(new Integer[]{3, 4, 8, 6, 7, 9}, new double[]{3.0, 4.0, 8.0, 6.0, 7.0, 9.0}, mh5);

        Heap<Integer> mh6= heapify(new Integer[] {2, 4, 3, 6, 7, 9, 8});
        Integer res6= mh6.poll();
        assertEquals(new Integer(2), res6);
        check(new Integer[]{3, 4, 8, 6, 7, 9}, new double[]{3.0, 4.0, 8.0, 6.0, 7.0, 9.0}, mh6);

        Heap<Integer> mh7= new Heap<Integer>();
        try {
            Integer k= mh7.poll();  fail("Didn't throw an exception");
        } catch (NoSuchElementException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than PCueException");
        }
    }

    @Test
    /** Test bubble-up and bubble-down with duplicate priorities. */
    public void test40testDuplicatePriorities() {
        // values should not bubble up or down past ones with duplicate priorities.
        // First two check bubble up
        Heap<Integer> mh1= heapify(new Integer[] {6}, new double[] {4.0});
        mh1.add(5, 4.0);
        check(new Integer[]{6, 5}, new double[]{4.0, 4.0}, mh1);

        Heap<Integer> mh2= heapify(new Integer[] {7, 6}, new double[] {4.0, 4.0});
        mh2.add(3, 4.0);
        check(new Integer[]{7, 6, 3}, new double[]{4.0, 4.0, 4.0}, mh2);

        // Check bubble up
        Heap<Integer> mh3= heapify(new Integer[] {5, 6, 7}, new double[] {4.0, 4.0, 4.0});
        mh3.poll();
        check(new Integer[]{7, 6}, new double[]{4.0, 4.0}, mh3);

        // Check bubble up
        Heap<Integer> mh4= heapify(new Integer[] {5, 7, 6, 8}, new double[] {4.0, 4.0, 4.0, 4.0});
        mh4.poll();
        check(new Integer[]{8, 7, 6}, new double[]{4.0, 4.0, 4.0}, mh4);

    }

    @Test
    /** Test updatePriority. */
    public void test50ChangePriority() {
        // First three: bubble up tests
        Heap<Integer> mh1= heapify(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh1.changePriority(5, 4.0);
        check(new Integer[]{1, 2, 3, 5, 6, 7, 9}, new double[]{1.0, 2.0, 3.0, 4.0, 6.0, 7.0, 9.0}, mh1);

        Heap<Integer> mh2= heapify(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh2.changePriority(2, 1.0);
        check(new Integer[]{1, 2, 3, 5, 6, 7, 9}, new double[]{1.0, 1.0, 3.0, 5.0, 6.0, 7.0, 9.0}, mh2);

        Heap<Integer> mh3= heapify(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh3.changePriority(5, 1.0);
        check(new Integer[]{1, 5, 3, 2, 6, 7, 9}, new double[]{1.0, 1.0, 3.0, 2.0, 6.0, 7.0, 9.0}, mh3);

        // second three: bubble down tests
        Heap<Integer> mh4= heapify(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh4.changePriority(2, 5.0);
        check(new Integer[]{1, 2, 3, 5, 6, 7, 9}, new double[]{1.0, 5.0, 3.0, 5.0, 6.0, 7.0, 9.0}, mh4);

        Heap<Integer> mh5= heapify(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh5.changePriority(2, 6.0);
        check(new Integer[]{1, 5, 3, 2, 6, 7, 9}, new double[]{1.0, 5.0, 3.0, 6.0, 6.0, 7.0, 9.0}, mh5);

        Heap<Integer> mh6= heapify(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh6.changePriority(1, 7.0);
        check(new Integer[]{2, 5, 3, 1, 6, 7, 9}, new double[]{2.0, 5.0, 3.0, 7.0, 6.0, 7.0, 9.0}, mh6);

        Heap<Integer> mh7= new Heap<Integer>();
        mh7.add(5, 5.0);
        try {
            mh7.changePriority(6, 5.0);  fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than IllegalArgumentException");
        }
    }

    @Test
    /** Test a few calls with Strings */
    public void test70Strings() {
        Heap<String> mh= new Heap<String>();
        check(new String[]{}, new double[]{}, mh);
        mh.add("abc", 5.0);
        check(new String[]{"abc"}, new double[]{5.0}, mh);
        mh.add(null, 3.0);
        check(new String[]{null, "abc"}, new double[]{3.0, 5.0}, mh);
        mh.add("", 2.0);
        check(new String[]{"", "abc", null}, new double[]{2.0, 5.0, 3.0}, mh);
        String p= mh.poll();
        check(new String[]{null, "abc"}, new double[]{3.0, 5.0}, mh);
        mh.changePriority(null, 7.0);
        check(new String[]{"abc", null}, new double[]{5.0, 7.0}, mh);
    }

    @Test
    /** Test using values in 0..999 and random values for the priorities.
     *  There will be duplicate priorities. */
    public void test90BigTests() {
        // The values to put in Heap
        int[] b= new int[1000];
        for (int k= 0; k < b.length; k= k+1) {
            b[k]= k;
        }

        Random rand= new Random(52); 

        // bp: priorities of the values
        double[] bp= new double[b.length];
        for (int k= 0; k < bp.length; k= k+1) {
            bp[k]= (int)(rand.nextDouble()*bp.length/3);
        }

        // Build the Heap and map to be able to get priorities easily
        Heap<Integer> mh= new Heap<Integer>();
        HashMap<Integer, Double> hashMap= new HashMap<Integer, Double>();
        for (int k= 0; k < b.length; k= k+1) {
            mh.add(b[k], bp[k]);
            hashMap.put(b[k], bp[k]);
        }

        // Poll the heap into array bpoll
        int[] bpoll= new int[b.length];
        pollHeap(mh, b);

        // Check that the priorities of the polled values are in order.
        Double previousPriority= hashMap.get(bpoll[0]);
        boolean inOrder= true;
        for (int k= 1; k < bpoll.length;  k= k+1) {
            Double p= hashMap.get(bpoll[k]);
            inOrder= inOrder  &&  previousPriority <= p;
            previousPriority= p;
        }
        boolean finalInOrder= inOrder;
        assertEquals("Polled values are in order", true, finalInOrder);
    }

    /** Poll all elements of m into b.
     * Precondition m and b are the same size. */
    public void pollHeap(Heap<Integer> m, int[] b) {
        for (int k= 0; k < b.length; k= k+1) {
            b[k]= m.poll();
        }
    }


}
