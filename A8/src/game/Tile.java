package game;

public class Tile {

    /** An enum representing the different types of Tiles that may appear in a
     * cavern.
     * 
     * @author eperdew */
    public enum Type {
        FLOOR, ORB, ENTRANCE,
        WALL {
            @Override
            public boolean isOpen() {
                return false;
            }
        };
        
        /**  Return true iff this Type of Tile is traversable. */
        public boolean isOpen() {
            return true;
        }
    }

    /** The row and column position of the GameNode */
    private final int row;
    private final int col;

    /** Amount of gold on this Node */
    private final int goldAmount;
    
    /** The Type of Tile this Node has */
    private Type type;
    private boolean goldPickedUp;
    
    /** Constructor: an instance with row r, column c, gold g, and Type t. */
    public Tile(int r, int c, int g, Type t) {
        row= r;
        col= c;
        goldAmount= g;
        type= t;
        goldPickedUp= false;
    }
    
    /** Return the amount of gold on this Tile. */
    public int gold() {
        return (goldPickedUp ? 0 : goldAmount);
    }

    /** Return the original gold on this tile. */
    public int getOriginalGold() {
        return goldAmount;
    }
    
    /** Return the row of this Tile. */
    public int getRow() {
        return row;
    }
    
    /** Return the column of this Tile. */
    public int getColumn() {
        return col;
    }
    
    /**  Return the Type of this Tile.  */
    public Type getType() {
        return type;
    }
    
    /** Set the Type of this Tile to t. */
    /* package */ void setType(Type t){
        type = t;
    }
    
    /**  Set the gold on this Node to 0 and return the amount "taken" */
    public int takeGold() {
        int result= gold();
        goldPickedUp= true;
        return result;
    }
}
