package graph;

import java.awt.Color;

/** A colorable object is an object that has a color field.
 * Getting the color should be open, but setting it is protected, so 
 * setting the color can be done only by the game files.
 * 
 * Color may or may not have a significance (if not, is just for show).
 * As such, significant color should not be changed while a run is proceeding.
 * @author MPatashnik
 *
 */
public interface Colorable {

	/** Return the color of this object. */
	Color getColor();
	
	/** Return true iff the color of this object has significance */
	boolean isColorSignificant();
}
