package game;

import java.util.Objects;

/** A Pair&lt;X,Y&gt; represents an immutable ordered pair of two Objects of types X
 * and Y respectively.
 * 
 * @author eperdew
 *
 * @param <X> The type of the first object in this Pair
 * @param <Y> The type of the second object in this Pair
 */
public final class Pair<X,Y> {
	X first;
	Y second;
	
	/** Constructor: a pair (x, y). */
	public Pair(X x, Y y) {
		first = x;
		second = y;
	}
	
	/** Return the first object in this Pair.  */
	public X getFirst() {
		return first;
	}
	
	/** Return the second object in this Pair. */
	public Y getSecond() {
		return second;
	}
	
	@Override public boolean equals(Object o) {
		if (!(o instanceof Pair<?,?>)) {
			return false;
		}
		Pair<?,?> p = (Pair<?,?>) o;
		return first.equals(p.first) && second.equals(p.second);
	}
	
	@Override public int hashCode() {
		return Objects.hash(first, second);
	}
	
}
