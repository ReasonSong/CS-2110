package graph;

import gui.GUI;

import java.io.File;
import java.util.*;
import java.util.concurrent.Semaphore;

/** Game starting methods. Also serves as a util holder */
public class Main {

	/** Student directory */
	public static final String studentDirectory = "";
	
	
	public static File file= null; // map being used --null if random map
	
	public static Graph graph= null; 
	public static GUI gui= null;
	
	/** Read in the name of a Map t start with.
	 * @throws IllegalArgumentException if args is null or has length 0.
	 */
	public static void main(String[] args) throws IllegalArgumentException {
			Graph b= Graph.randomBoard(Math.abs((new Random()).nextLong()));
			gui= new GUI(b);
			b.setGUI(gui);
		}
	
	/** Store in field graph a random board from seed s 
	 * and create the GUI using it. */
    public void randomBoard(long s) {
        file= null;
        graph= Graph.randomBoard(s);
        gui= new GUI(graph);
    }

	/** Return the sum of the natural numbers in 0..i, recursively!
	 * (mathematically, that's 0 if i = -1) */
	public static int sumTo(int i) {
		if (i < 0) return 0;
		return sumToHelper(i,0);
	}

	/** return s + sum of 0..i. 
	 * Written tail recursively, but it does not matter in Java*/
	private static int sumToHelper(int i, int s) {
		if (i == 0) return s;
		return sumToHelper(i-1, s+i);
	}

	/** Return s with quotes added around it.
	 * Used in JSON creation methods throughout project. */
	public static String addQuotes(String s) {
		return "\"" + s + "\"";
	}

	/** Return a random element of elms (null if elms is empty).
	 * Synchronizes on {@code elms} to prevent concurrent modification. */
	public static <T> T randomElement(Collection<T> elms) {
		T val = null;
		synchronized(elms) {
			if (elms.isEmpty())
				return null;
			Iterator<T> it = elms.iterator();
			for (int i= 0; i < (int)(Math.random() * elms.size()) + 1; i++) {
				val= it.next();
			}
		}
		return val;
	}

}
