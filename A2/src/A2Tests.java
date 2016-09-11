import static org.junit.Assert.*;

import org.junit.Test;

public class A2Tests {

    @Test
    public void testIsDoubled() {
        assertEquals(true,  A2.isDoubled(""));
        assertEquals(true,  A2.isDoubled("  "));
        assertEquals(false, A2.isDoubled("xxx"));
        assertEquals(true,  A2.isDoubled("xxxx"));
        assertEquals(true,  A2.isDoubled("hellohello"));
        assertEquals(false, A2.isDoubled("helloworld"));
        assertEquals(false, A2.isDoubled("helloxhello"));
    }

    @Test
    public void testReverse() {
        assertEquals("",    A2.reverse(""));
        assertEquals(" ",   A2.reverse(" "));
        assertEquals("c",   A2.reverse("c"));
        assertEquals("cb",  A2.reverse("bc"));
        assertEquals("cba", A2.reverse("abc"));
        assertEquals("ablewasIereIsawelba", A2.reverse("ablewasIereIsawelba"));
    }

    @Test
    public void testEncode() {
        assertEquals("", A2.encode("", "", ""));
        assertEquals("", A2.encode("", "bc", "dd"));
        assertEquals("hello world", A2.encode("hello world", "", ""));
        assertEquals("hello world", A2.encode("hello world", "abc", "lmn"));
        assertEquals("xelly wyrlz", A2.encode("hello world", "hod", "xyz"));
        assertEquals("..... .....", A2.encode("hello world", "helowrd", "......."));
    }

    @Test
    public void testDeduplicate() {
        assertEquals("",                A2.deduplicate(""));
        assertEquals("x",               A2.deduplicate("x"));
        assertEquals(" ",               A2.deduplicate("  "));
        assertEquals("x",               A2.deduplicate("xxxxxx"));
        assertEquals("xy",              A2.deduplicate("xyxyxyxy"));
        assertEquals("hello",           A2.deduplicate("hellohellohello"));
        assertEquals("hellohelloworld", A2.deduplicate("hellohelloworld"));
        assertEquals("hellohel",        A2.deduplicate("hellohel"));
    }

    @Test
    public void testEvaluate() {
    	
    	assertEquals(-1, A2.findNextNonSpace(" ", 0));
    	assertEquals(0, A2.findNextSpace(" ", 0));
    	assertEquals(3, A2.findNextSpace("123 6 ", 0));
    	assertEquals(1, A2.findNextNonSpace(" ab cd", 0));
    	assertEquals(2, A2.findNextNonIntChar("12+3" , 0));
    	assertEquals(6, A2.getNextInt(" 6 "));
    	
        assertEquals(9,   A2.evaluate("  9"));
        assertEquals(7,   A2.evaluate("3 + 4"));
        assertEquals(-1,  A2.evaluate("3 - 4"));
        assertEquals(125, A2.evaluate("100 - 25+50"));
        assertEquals(679, A2.evaluate("   7   +   6   +    666  "));
    }

}

