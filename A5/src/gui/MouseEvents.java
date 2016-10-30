package gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/** Contains methods that respond to mouse events in a Square */

public class MouseEvents extends MouseInputAdapter {

    public void mouseClicked(MouseEvent e) {
        Object ob= e.getSource();
        if (ob instanceof Square) ((Square)ob).mark();
    }
    
	public void mouseEntered(MouseEvent e){
		Object ob = e.getSource();
        if (ob instanceof Square) ((Square)ob).enter();
	}

	public void mouseExited(MouseEvent e){
		Object ob = e.getSource();
        if (ob instanceof Square) ((Square)ob).exit();
	}
}