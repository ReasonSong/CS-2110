/** An instance maintains a time of day */
public class Time {

    private int hr;    //hour of the day, in 0..23
    private int min;   // minute of the hour, in 0..59

    /** Constructor 1: an instance with hour h and minute m.
     *  Precondition: ...*/
    public Time(int h, int m) {
        hr= h;
        min= m;
    }
    
    /** Constructor: an instance with m minutes of the day.
     *  Precondition: m in 0..23*60 + 59*/
    public Time(int m) {
        //TODO 
    }

    /** Return the hour of the day. */
    public int getHour() {
        return hr;
    }

    /** Return the minute of the hour. */
    public int getMinute() {
        return min;
    }

    /** Set the hour of the day to h.
     * Precondition: h in 0..23 */
    public void setHour(int h) {
        assert 0 <= h  &  h <= 23;
        hr= h;
    }

    /** Return a representation of this time, e.g. 09:05.
     * Each of hour and minute must be 2 characters. */
    public String toString() {
        return prepend(hr) + ":" + prepend(min);
    }
    
    /** Return i with preceding 0, if necessary, to make it two chars */
    private String prepend(int i) {
        if (0 <= i  &&  i < 10) return "0" + i;
        return "" + i;
    }
}
