import java.util.Set;

import common.StringUtil;
import common.Util;
import common.types.Tuple1;

/** A instance represents a human and their health. 
 * @author Mshnik*/
public class Human extends Tuple1<String>{

    /** The possible Bug-related states of a human. */
    enum State{  // The names indicate the state.
        HEALTHY,
        BUGGED,
        DEAD,
        IMMUNE
    }

    private final Network graph; // The network to this human belongs

    private int health;    // Amount of health this human has. >= 0.
                           // 0 means dead, any other number means alive
    private State state;        // State of this human
    private int stepGotBugged= -1;   // Time step in which this human got sick (-1 if never been sick)
    private int stepGotImmune= -1; // Time step in which this human got immune (-1 if not immune)
    private int stepDied= -1;      // Time step in which this human died (-1 if not dead)

 
    /** Constructor: a healthy Human with name n and health h, added to graph g.
     * Precondition: The new human is not in g, and their name is distinct from the name
     *               of any other human in g. This is because the name is used for
     *               equality and hashing. */
    public Human(String n, Network g, int h) {
        super(StringUtil.toPronounCase(n));
        health= h;
        state= State.HEALTHY;
        graph= g;
        graph.addVertex(this);
    }

    /** Return a representation of this Human. */
    public @Override String toString(){
        return super.toString() + " - " + state;
    }

    /** Return the name of this human. */
    public String getName(){
        return _1;
    }

    /** Give this human the bug during step currentStep.
     * Throw a RuntimeException if this human is not HEALTHY. */
     public void getBug(int currentStep) {
        if (state != State.HEALTHY) {
            throw new RuntimeException(state + " human can't become sick");
        }
        state= State.BUGGED;
        stepGotBugged= currentStep;
    }

     /** Make this human immune during step currentStep.
      * Throw a RuntimeException if this human is immune or dead. */
     public void becomeImmune(int currentStep) {
        if (state == State.IMMUNE || state == State.DEAD) {
            throw new RuntimeException(state + " human can't become immune");
        }
        state= State.IMMUNE;
        stepGotImmune= currentStep;
    }

     /** Decrement the health of this human in step currentStep. If its health
      * becomes 0, the human dies.
      * Throw a RuntimeException if this human is not sick. */
     public void reduceHealth(int currentStep) {
        if (state != State.BUGGED) {
            throw new RuntimeException(state + " human can't lose health");
        }
        health--;
        if (health == 0) {
            state= State.DEAD;
            stepDied= currentStep;
        }
    }

    /** Return the state of this human. */
    public State getState() {
        return state;
    }

    /** Return "This human is alive". */
    public boolean isAlive() {
        return state != State.DEAD;
    }

    /** @return true iff this human is dead */
    public boolean isDead() {
        return !isAlive();
    }

    /** Return "This human is healthy. */
    public boolean isHealthy() {
        return state == State.HEALTHY;
    }

    /** Return "This person is immune". */
    public boolean isImmune() {
        return state == State.IMMUNE;
    }

    /** Return "This human has the bug". */
    public boolean hasBug() {
        return state == State.BUGGED;
    }

    /** Return the time step in which this human got the bug" (-1 if never been sick). */
    public int getFrameGotSick() {
        return stepGotBugged;
    }

    /** Return the time step in which this human got immune" (-1 if not immune). */
    public int getFrameGotImmune() {
        return stepGotImmune;
    }

    /** Return the time step in which this human died" (-1 if not dead). */
    public int getFrameDied() {
        return stepDied;
    }

    /** Return the neighbors of this human. */
    public Set<Human> getNeighbors() {
        return graph.neighborsOf(this);
    }

    /** Return a random neighbor of this human */
    public Human getRandomNeighbor(){
        return Util.randomElement(graph.neighborsOf(this));
    }
}
