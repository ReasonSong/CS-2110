package a1PhD;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhDTester {

	@Test
	public void testConstructor1() {
		
		PhD Summer = new PhD("Hongshu Ye", 'F', 9, 2016);
		
		assertEquals("Hongshu Ye", Summer.getName());
		assertEquals(true, Summer.isFemale());
		assertEquals(9, Summer.getMonth());
		assertEquals(2016, Summer.getYear());
		assertEquals(null, Summer.advisor1());
		assertEquals(null, Summer.advisor2());
		assertEquals(0, Summer.getAdviseeNum());
		
		PhD Reason = new PhD("Ruochen Song", 'M', 9, 2015);
		
		assertEquals("Ruochen Song", Reason.getName());
		assertEquals(false, Reason.isFemale());
		assertEquals(9, Reason.getMonth());
		assertEquals(2015, Reason.getYear());
		assertEquals(null, Reason.advisor1());
		assertEquals(null, Reason.advisor2());
		assertEquals(0, Reason.getAdviseeNum());
		
	}
	
	

}
