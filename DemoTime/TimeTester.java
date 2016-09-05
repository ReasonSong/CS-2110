import static org.junit.Assert.*;

import org.junit.Test;


public class TimeTester {

    @Test
    public void testConstructor1() {
        Time t= new Time(9, 5);
        // assertEquals(expected val, computed val);
        assertEquals(9, t.getHour());
        assertEquals(5, t.getMinute());
    }
    
    @Test
    public void testSetters() {
        Time t= new Time(23, 2);
        t.setHour(22);
        // assertEquals(expected val, computed val);
        assertEquals(22, t.getHour());
    }
    

    @Test
    public void testToString() {
        Time t= new Time(23, 2);
        // assertEquals(expected val, computed val);
        assertEquals("23:02", t.toString());

        t= new Time(0, 31);
        // assertEquals(expected val, computed val);
        assertEquals("00:31", t.toString());
    }

}
