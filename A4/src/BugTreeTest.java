import static org.junit.Assert.*;
import static common.JUnitUtil.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.junit.Test;

public class BugTreeTest {

    private static Network n;
    private static Human[] humans;

    @BeforeClass
    public static void setup(){
        n= new Network();
        humans= new Human[]{new Human("A", n, 0), new Human("B", n, 0), new Human("C", n, 0),
                new Human("D", n, 0), new Human("E", n, 0), new Human("F", n, 0),
                new Human("G", n, 0), new Human("H", n, 0), new Human("I", n, 0),
                new Human("J", n, 0), new Human("K", n, 0), new Human("L", n, 0)
        };
    }

    @Test
    public void testBuiltInGetters(){
        BugTree dt= new BugTree(humans[0]);
        assertEquals(humans[0], dt.getRoot());
        assertEquals(0, dt.numberOfChildren());
        assertTrue(dt.copyOfChildren().isEmpty());
    }

    @Test
    public void testAdd() {
        BugTree dt= new BugTree(humans[0]); 

        //Test add to root
        BugTree dt2= dt.add(humans[0], humans[1]);
        assertEquals(humans[1], dt2.getRoot());
        assertEquals(1, dt.numberOfChildren());
        assertEquals(0, dt2.numberOfChildren());
        assertTrue(dt.copyOfChildren().contains(dt2));
        assertTrue(dt2.copyOfChildren().isEmpty());

        //Test add to non-root
        BugTree dt3= dt.add(humans[1], humans[2]);
        assertEquals(humans[2], dt3.getRoot());
        assertEquals(1, dt.numberOfChildren());
        assertEquals(1, dt2.numberOfChildren());
        assertEquals(0, dt3.numberOfChildren());
        assertTrue(dt.copyOfChildren().contains(dt2));		
        assertTrue(dt2.copyOfChildren().contains(dt3));
        assertTrue(dt3.copyOfChildren().isEmpty());

        //Test add second child
        BugTree dt4= dt.add(humans[0], humans[3]);
        assertEquals(humans[3], dt4.getRoot());
        assertEquals(2, dt.numberOfChildren());
        assertEquals(1, dt2.numberOfChildren());
        assertEquals(0, dt3.numberOfChildren());
        assertEquals(0, dt4.numberOfChildren());
        assertTrue(dt.copyOfChildren().contains(dt2));	
        assertTrue(dt.copyOfChildren().contains(dt4));		
        assertTrue(dt2.copyOfChildren().contains(dt3));
        assertTrue(dt3.copyOfChildren().isEmpty());
        assertTrue(dt4.copyOfChildren().isEmpty());
    }

    @Test
    public void testDepth() {
        BugTree dt= new BugTree(humans[0]); 
        dt.add(humans[0], humans[1]);
        dt.add(humans[0], humans[2]);
        dt.add(humans[1], humans[3]);
        dt.add(humans[1], humans[4]);
        dt.add(humans[3], humans[5]);
        assertEquals(0, dt.depthOf(humans[0]));
        assertEquals(1, dt.depthOf(humans[1]));
        assertEquals(2, dt.depthOf(humans[3]));
        assertEquals(3, dt.depthOf(humans[5]));
        assertEquals(3, dt.maxDepth());
    }
    
    @Test
    public void testWidth() {
        BugTree dt= new BugTree(humans[0]); 
        dt.add(humans[0], humans[1]);
        dt.add(humans[0], humans[2]);
        dt.add(humans[1], humans[3]);
        dt.add(humans[1], humans[4]);
        dt.add(humans[2], humans[5]);
        dt.add(humans[2], humans[6]);
        dt.add(humans[2], humans[7]);
        dt.add(humans[3], humans[8]);
        dt.add(humans[4], humans[9]);
        dt.add(humans[5], humans[10]);   
        assertEquals(2, dt.widthAtDepth(1));
        assertEquals(5, dt.widthAtDepth(2));
        assertEquals(3, dt.widthAtDepth(3));
        assertEquals(0, dt.widthAtDepth(4));
    }
    
    @Test
    public void testBugRoute() {
        BugTree dt= new BugTree(humans[0]); 
        dt.add(humans[0], humans[1]);
        dt.add(humans[0], humans[2]);
        dt.add(humans[1], humans[3]);
        dt.add(humans[1], humans[4]);
        dt.add(humans[2], humans[5]);
        dt.add(humans[2], humans[6]);
        dt.add(humans[2], humans[7]);
        dt.add(humans[3], humans[8]);
        dt.add(humans[4], humans[9]);
        dt.add(humans[5], humans[10]);
        LinkedList<Human> result = new LinkedList<Human>();
        result.add(humans[0]);
        assertEquals(true, dt.bugRouteTo(humans[0]).equals(result));
        result.add(humans[1]);
        assertEquals(true, dt.bugRouteTo(humans[1]).equals(result));
        result.add(humans[3]);
        assertEquals(true, dt.bugRouteTo(humans[3]).equals(result));
        result.add(humans[8]);
        assertEquals(true, dt.bugRouteTo(humans[8]).equals(result));
        assertEquals(null, dt.bugRouteTo(humans[11]));
    }
    
    @Test
    public void testSharedForebareOf() {
        BugTree dt= new BugTree(humans[0]); 
        dt.add(humans[0], humans[1]);
        dt.add(humans[0], humans[2]);
        dt.add(humans[1], humans[3]);
        dt.add(humans[1], humans[4]);
        dt.add(humans[2], humans[5]);
        dt.add(humans[2], humans[6]);
        dt.add(humans[2], humans[7]);
        dt.add(humans[3], humans[8]);
        dt.add(humans[4], humans[9]);
        dt.add(humans[5], humans[10]);
        assertEquals(humans[0], dt.sharedForebearOf(humans[0], humans[0]));
        assertEquals(humans[0], dt.sharedForebearOf(humans[0], humans[1]));
        assertEquals(humans[1], dt.sharedForebearOf(humans[3], humans[4]));
        assertEquals(humans[1], dt.sharedForebearOf(humans[3], humans[9]));
        assertEquals(humans[3], dt.sharedForebearOf(humans[3], humans[8]));
        assertEquals(null, dt.sharedForebearOf(humans[0], humans[11]));
    }
}
