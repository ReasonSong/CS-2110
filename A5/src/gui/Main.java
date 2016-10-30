/* NetId(s): rs2352, hy483. Time spent: hh hours, mm minutes. */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import controller.*;
import model.*;

import gui.*;

public class Main {
	
	public static void main(String[] args) {

		Game g = new Game();
		g.addListener(new Board());
	
//		Controller playerX; // = createController(Player.X);
//		Controller playerO; // = createController(Player.O);
		
		//g.addListener(playerX);
		//g.addListener(playerO);
		
	}
}

