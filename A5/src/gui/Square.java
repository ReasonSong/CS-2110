package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import model.Game;
import model.GameListener;
import model.Player;


/** An instance is a JPanel of size (WIDTH,HEIGHT). Clicking
  on the square creates a X/O mark on it.
  */
public class Square extends JPanel implements GameListener{
    
	public static final int HEIGHT = 30;  // square height
    public static final int WIDTH = 30;   // square width 
    
    private int x, y; // Coordinates of square on board
    private Player turn;
    private boolean clicked = false; 
    private boolean entered = false; 
    
    /** Constructor: a square at (x,y) */
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.turn = Player.X;
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }
    
    /* paint this square using g. The system calls
     paint whenever the square has to be redrawn.*/
    public void paint(Graphics g) {
        
    	// Draw the Edge
        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH-1,HEIGHT-1);
    	
        // Draw and fill the square
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH-1, HEIGHT-1);
        
        // Draw the X/O mark
        if (clicked) {
            
        	g.setColor(Color.black);
        	if (turn == Player.X) {g.drawLine(0, 0, WIDTH-1, HEIGHT-1); g.drawLine(0, HEIGHT-1, WIDTH-1, 0);}
        	else g.drawOval(0, 0, WIDTH-2, HEIGHT-2);
        	
        } else if (entered) {	// Show the X mark
        	
        	g.setColor(Color.black);
        	g.drawLine(3, 3, WIDTH-5, HEIGHT-5);
        	g.drawLine(3, HEIGHT-5, WIDTH-5, 3);
        }
    }
    
    /** Mark an X/O after been clicked */
    public void mark() {  
        clicked = true;
        repaint();
    }
    
    /** Show an X mark when the mouse enters */
    public void enter() {  
    	entered = true;
        repaint();
    }
    
    /** Remove the X mark when the mouse exits */
    public void exit() {
    	entered = false;
        repaint();
    }
    
    /** Reset the whole board */
    public void clear() {
        clicked = false;
        repaint();
    }

	@Override
	public void gameChanged(Game g) {
		turn = g.nextTurn();
	}
}