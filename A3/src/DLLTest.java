import static org.junit.Assert.*;

import org.junit.Test;

public class DLLTest {

	@Test
	public void testConstructor() {
		DLL<Integer> dll = new DLL<Integer>();
		assertEquals("[]", dll.toString());
		assertEquals("[]", dll.toStringRev());
		assertEquals(0, dll.size());
	}
	
	@Test
	public void testAdd() {
		DLL<Integer> dll = new DLL<Integer>();
		dll.add(5);
		assertEquals("[5]", dll.toString());
		assertEquals("[5]", dll.toStringRev());
		assertEquals(1, dll.size());
		dll.add(8);
		assertEquals("[5, 8]", dll.toString());
		assertEquals("[8, 5]", dll.toStringRev());
		assertEquals(2, dll.size());
		dll.add(12);
		assertEquals("[5, 8, 12]", dll.toString());
		assertEquals("[12, 8, 5]", dll.toStringRev());
		assertEquals(3, dll.size());
		dll.add(17);
		assertEquals("[5, 8, 12, 17]", dll.toString());
		assertEquals("[17, 12, 8, 5]", dll.toStringRev());
		assertEquals(4, dll.size());
	}
	
	@Test
	public void testGetNodeValue() {
		DLL<Integer> dll = new DLL<Integer>();
		dll.add(5);
		dll.add(8);
		dll.add(12);
		dll.add(17);
		dll.add(9);
		assertEquals(5, dll.get(0).intValue());
		assertEquals(8, dll.get(1).intValue());
		assertEquals(12, dll.get(2).intValue());
		assertEquals(17, dll.get(3).intValue());
		assertEquals(9, dll.get(4).intValue());
	}
	
	@Test
	public void testSetNodeValue() {
		DLL<Integer> dll = new DLL<Integer>();
		dll.add(5);
		dll.add(8);
		dll.add(12);
		dll.add(17);
		dll.add(9);
		assertEquals(17, dll.set(3, 9).intValue());
		assertEquals(9, dll.set(3, 8).intValue());
		assertEquals("[5, 8, 12, 8, 9]", dll.toString());
	}
	
	@Test
	public void testAddBefore() {
		DLL<Integer> dll = new DLL<Integer>();
		dll.add(5);
		dll.add(8);
		dll.add(12);

		dll.add(0, 6);
		assertEquals("[6, 5, 8, 12]", dll.toString());
		assertEquals("[12, 8, 5, 6]", dll.toStringRev());
		assertEquals(4, dll.size());
		
		dll.add(4, 19);
		assertEquals("[6, 5, 8, 12, 19]", dll.toString());
		assertEquals("[19, 12, 8, 5, 6]", dll.toStringRev());
		assertEquals(5, dll.size());
	}
	
	@Test
	public void testRemove() {
		DLL<Integer> dll = new DLL<Integer>();
		dll.add(5);
		dll.add(8);
		dll.add(12);
		
		assertEquals(5, dll.remove(0).intValue());
		assertEquals("[8, 12]", dll.toString());
		assertEquals("[12, 8]", dll.toStringRev());
		assertEquals(2, dll.size());
		
		assertEquals(12, dll.remove(1).intValue());
		assertEquals("[8]", dll.toString());
		assertEquals("[8]", dll.toStringRev());
		assertEquals(1, dll.size());
		
		assertEquals(8, dll.remove(0).intValue());
		assertEquals("[]", dll.toString());
		assertEquals("[]", dll.toStringRev());
		assertEquals(0, dll.size());
	}
	
	@Test
	public void testThrowException() {
		DLL<Integer> dll = new DLL<Integer>();
		try {dll.getNode(-1); fail();}
		catch (IndexOutOfBoundsException e) {}
		catch (Throwable t) {fail();}
		
		try {dll.get(-1); fail();}
		catch (IndexOutOfBoundsException e) {}
		catch (Throwable t) {fail();}
		
		try {dll.set(-1, null); fail();}
		catch (IndexOutOfBoundsException e) {}
		catch (Throwable t) {fail();}
		
		try {dll.add(-1, 10); fail();}
		catch (IndexOutOfBoundsException e) {}
		catch (Throwable t) {fail();}
		
		try {dll.remove(-1); fail();}
		catch (IndexOutOfBoundsException e) {}
		catch (Throwable t) {fail();}
	}

}
