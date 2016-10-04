import static org.junit.Assert.*;
import static common.JUnitUtil.*;

import java.util.Arrays;
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

}
