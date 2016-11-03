package gui;

import java.awt.*;
import javax.swing.*;

import model.Game;
import model.GameListener;
import model.Player;


/** An instance is a JPanel of size (WIDTH,HEIGHT). Clicking
  on the square creates a X/O mark on it.
  */
public class Square extends JPanel implements GameListener{
    
	public static final int HEIGHT = 40;  // square height
    public static final int WIDTH = 40;   // square width 
    
    private int x, y; // Coordinates of square on board
    private GUIController pX, pO;
    private Player turn;
    private boolean clicked = false; 
    private boolean entered = false; 
    
    /** Constructor: a square at (x,y) */
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        pX = null;
        pO = null;
        this.turn = Player.X;
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }
    
//    public int getX(){
//    	return x;
//    }
//    
//    public int getY(){
//    	return y;
//    }
    
    public void setPlayer(GUIController pX, GUIController pO){
    	this.pX = pX;
    	this.pO = pO;
    }
    
    /* paint this square using g. The system calls
     paint whenever the square has to be redrawn.*/
    public void paint(Graphics g) {
        
    	// Draw the Edge
        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH-1, HEIGHT-1);
    	
        // Draw and fill the square
        if ((x+y)%2 == 0) g.setColor(Color.white);
        else g.setColor(Color.gray);
        g.fillRect(0, 0, WIDTH-1, HEIGHT-1);
        
        // Draw the X/O mark
        if (clicked) {
            
        	g.setColor(Color.black);
        	if (turn == Player.X) {g.drawLine(0, 0, WIDTH-1, HEIGHT-1); g.drawLine(0, HEIGHT-1, WIDTH-1, 0);}
        	else g.drawOval(1, 1, WIDTH-4, HEIGHT-4);
        	
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
        paintImmediately(this.getBounds());
    	// Update move to the game
        if (turn == Player.X && pX != null)  pX.updateLoc(x, y);
        else if (turn == Player.O && pO != null) pO.updateLoc(x, y);
    }
    
    /** Show an X mark when the mouse enters */
    public void enter() {  
    	entered = true;
    	if (!clicked) {
    		repaint();
    		paintImmediately(this.getBounds());
    	}
    }
    
    /** Remove the X mark when the mouse exits */
    public void exit() {
    	entered = false;
        if(!clicked ) {
        	repaint();
        	paintImmediately(this.getBounds());
        }
    }
    
    /** Reset the whole board */
    public void clear() {
        clicked = false;
        repaint();
        paintImmediately(this.getBounds());
    }

	@Override
	public void gameChanged(Game g) {
		if(!clicked) turn = g.nextTurn();
	}
	
}