package graph;

import org.json.JSONString;

/** Interface GraphElement can be implemented by any class that wants to be drawn
 * on the GUI. Through the implementation of this interface, the
 * implementing class will return values on what string name to draw on the GUI,
 * where to draw that name, what color it is, and if a truck is currently on it.
 * 
 * All graph elements must also be JSON-able and must override function toJSONString.
 * @author MPatashnik
 */
public interface GraphElement extends JSONString, Colorable {

	/** The key for the x field for JSON implementations. */
	public static final String X_TOKEN = "x";
	/** The key for the y field for JSON implementations. */
	public static final String Y_TOKEN = "y";
	
	/** The key for the name field for JSON implementations. */
	public static final String NAME_TOKEN = "name";
	
	/** The key for the color field for JSON implementations.  */
	public static final String COLOR_TOKEN = "color";
	
	/** The key for the location field for JSON implementations. 
	 * May be a single value (in parcel), or an array (in edge) */
	public static final String LOCATION_TOKEN = "location";
	
	/** The key for the destination field for JSON implementations. */
	public static final String DESTINATION_TOKEN = "destination";
	
	/** The key for the length field for JSON implementations. */
	public static final String LENGTH_TOKEN = "length";
	
	/** The Name this Object has when drawn on the GUI */
	public String getMappedName();
	
	/** Return the x-coordinate of this Object's string drawing relative to the object. */
	public int getRelativeX();
	
	/** Return the y-coordinate of this Object's string drawing relative to the object. */
	public int getRelativeY();
	
	/** Update the location of this on the gui (change it to (x, y)). */
	public void updateGUILocation(int x, int y);
	
	/** Return the graph to which this GraphElement belongs. */
    public Graph getGraph();
}
