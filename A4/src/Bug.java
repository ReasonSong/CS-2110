import io.ScannerUtils;

import java.util.Scanner;
import java.util.Set;

import common.Util;
import common.types.Tuple;
import common.types.Tuple5;

/** An instance represents a bug spreading and ultimately dying out among
 * a limited population (or killing everyone).
 * <br>
 * Each Bug is created on a Network of humans and with a chosen first human.
 * Bug is runnable, but for the purposes of this project, it does not need
 * to be run on a separate thread.
 * @author MPatashnik
 */
public class Bug implements Runnable {

    private Network network; // The graph on which this bug is running. */
    private BugTree tree; // The tree representing this bug
    private int steps; // Number of time steps this bug took to create dt.
    private Statistics statistics; // The bug model:
    // Statistics that determine the spread of the bug.

    /** How many chars to print per line in the running section. */
    private static final int RUNNING_CHAR_COUNT_MAX= 50;

    /** Used in printing the run progress. */
    private int runningCharCount= 7;

    /** Constructor: a new Bug on network nw with first infected human fp
     *  and bug model s.    */
    public Bug(Network nw, Human fp, Statistics s){
        steps= 0;
        network= nw;
        fp.getBug(0);
        tree= new BugTree(fp);
        statistics= s;
    }

    /** Run the bug simulation until no infected humans remain.
     *  Print out info about running.*/
    public @Override void run(){
        System.out.print("Running");
        while (network.getHumansOfType(Human.State.BUGGED).size() > 0) {
            step();
        }
        System.out.println("Done.\n");
    }


    /** Perform a single step on the bug, using bug model statistics.
     * First, infected humans may become immune with a certain probability.
     * Second, infected humans become less healthy by 1, and if their health reaches 0, they die.
     * Third, infected humans may spread the bug to one neighbor, with a certain probability.
     */
    private void step() {
        Set<Human> humans= network.vertexSet();
        System.out.print(".");
        runningCharCount++;
        if (runningCharCount > RUNNING_CHAR_COUNT_MAX) {
            System.out.print("\n");
        }

        // For each infected human , make them immune with a certain probability
        for (Human p : humans) {
            if (p.hasBug() && statistics.humanBecomesImmune()){
                p.becomeImmune(steps);
            }
        }

        // For each infected human, deduct 1 from health and make dead if health becomes 0
        for (Human p : humans) {
            if (p.hasBug()) {
                p.reduceHealth(steps);
            }
        }

        // For each infected human, spread the bug to one random neighbor with a
        // certain probability.
        for (Human p : humans) {
            if (p.hasBug()) {
                Human n= p.getRandomNeighbor();
                if (n != null  &&  n.isHealthy()  &&  statistics.bugSpreadsToHuman()) {
                    n.getBug(steps);
                    tree.add(p, n);
                }
            }
        }

        steps= steps + 1;
    }

    /** Read the five statistic arguments from the console.
     * Return a Tuple5, with the following components:
     * 		<br> - size: the number of humans in the network
     * 		<br> - maxHealth: how much health each human starts with
     * 		<br> - connectionProbability: probability that two humans are connected in the network
     * 		<br> - infectionProbability: probability that an infected human spreads the sickness to a neighbor in one step
     * 		<br> - immunizationProbability: probability that an infected human becomes immune in one  step
     */
    private static Tuple5<Integer, Integer, Double, Double, Double> readArgs() {
        Scanner scanner= ScannerUtils.defaultScanner();
        int size = ScannerUtils.get(Integer.class, scanner, "Enter the size of the population: ",
                "Please enter a positive non-zero integer", (i) -> i > 0);
        int maxHealth= ScannerUtils.get(Integer.class, scanner, 
                "Enter the amount of health for each human: ",
                "Please enter a positive non-zero integer", (i) -> i > 0);
        double connectionProb= ScannerUtils.get(Double.class, scanner, 
                "Enter the probability of a connection: ",
                "Please enter a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        double infectedProb= ScannerUtils.get(Double.class, scanner, 
                "Enter the probability of becoming infected: ",
                "Please enter a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        double immunizationProb= ScannerUtils.get(Double.class, scanner, 
                "Enter the probability of becoming immune: ",
                "Please enter a double in the range [0,1]", (d) -> d >= 0 && d <= 1);
        scanner.close();
        return Tuple.of(size, maxHealth, connectionProb, infectedProb, immunizationProb);
    }


    /** Run Bug on the arguments listed in args.
     * If args does not match the pattern below, read in arguments via the console
     * by using readArgs().
     * 
     * Then, call bug.run() and create a BugFrame showing the created BugTree.
     * 
     * args should be an array of [size, maxHealth, connection probability, 
     * 						infected probability, immunization probability],
     * 		or unused (any value). If not used, the user is prompted for input in the console.
     */
    public static void main(String[] args) {
        //Get arguments
        int size= 10;
        int maxHealth= 5;
        double connectionProbability= 0.7;
        double infectedProbability= 0.5;
        double immunizationProbability= 0.1;

        try {
            //Attempt to read from args array passed in
            size= Integer.parseInt(args[0]);
            maxHealth= Integer.parseInt(args[1]);
            connectionProbability= Double.parseDouble(args[2]);
            infectedProbability= Double.parseDouble(args[3]);
            immunizationProbability= Double.parseDouble(args[4]);
        } catch (Exception e) {
            //If too few or wrong type, read from scanner
            Tuple5<Integer, Integer, Double, Double, Double> args2= readArgs();
            size= args2._1;
            maxHealth= args2._2;
            connectionProbability= args2._3;
            infectedProbability= args2._4;
            immunizationProbability= args2._5;
        }

        //Set defaults and create the Network, Statistics, and Bug objects
        System.out.print("\nSetting up ");
        System.out.print(".");
        Network n= new Network(size, connectionProbability, maxHealth);
        System.out.print(".");
        Statistics s= new Statistics(infectedProbability, immunizationProbability);
        System.out.print(".");
        Bug d= new Bug(n, Util.randomElement(n.vertexSet()), s);
        System.out.println("Done.");

        d.run();
        System.out.println(d.tree.toStringVerbose() + "\n");
        for (Human p : d.network.getHumansOfType(Human.State.HEALTHY)) {
            System.out.println(p);
        }
        BugGUI.show(d.tree, d.steps);
    }
}
