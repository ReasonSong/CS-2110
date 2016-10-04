/** An instance contains the probabilities that a human gets a bug 
 * and a human becomes immune in a time step.
 * 
 * @author Mshnik */
public class Statistics {

    private double infectionChance; // Probability of getting the bug. In range [0, 1]

    private double immunizationChance; //Probability of immunization. In range [0, 1]

    /** Constructor: an instance with infection probability cp and
     * immunization probability ip.
     * Precondition: 0 <= cp, ip <= 1. */
    public Statistics(double cp, double ip) {
        infectionChance= cp;
        immunizationChance= ip;
    }

    /** Return true if a new random number is less than the probability
     * of contagion. */
    public boolean bugSpreadsToHuman(){
      return Math.random() < infectionChance;
    }

    /** Return true if a new random number is less than the probability
     * of becoming immune. */
    public boolean humanBecomesImmune(){
      return Math.random() < immunizationChance;
    }
}
