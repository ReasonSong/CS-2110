/* NetId(s): rs2352, hy483. Time spent: hh hours, mm minutes. */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import controller.*;
import model.*;

public class Main {
	
	public static void main(String[] args) {

		Game g = new Game();	// X goes first
		
		GUIController playerX = new GUIController(Player.X, g);
		GUIController playerO = new GUIController(Player.O, g);
		Board b = new Board(playerX, playerO);
		
		g.addListener(b);
		
		for (int i = 0; i < 9; ++ i){
			for (int j = 0; j < 9; ++ j) g.addListener(b.getSqur(i, j));
		}
		
		g.addListener(playerX);
		g.addListener(playerO);
		
	}
}

