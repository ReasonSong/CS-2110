package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import model.Game;
import model.GameListener;
import model.Player;
import controller.Controller;
import controller.DumbAI;
import controller.RandomAI;
import controller.SmartAI;

public class Board extends JFrame implements ActionListener, GameListener{
	
	private Game game;
	private Controller playerX, playerO;

	Box  b = new Box(BoxLayout.Y_AXIS);		// Box for the board
	Box[] row = new Box[9];					// Box for each row of the play board
	Box controlBox = new Box(BoxLayout.Y_AXIS);	// Box for all the control components on the right side
	Box checkBox = new Box(BoxLayout.X_AXIS);	// Box for all check boxes
	Box xBox = new Box(BoxLayout.Y_AXIS);	// Box for player X check boxes
	Box oBox = new Box(BoxLayout.Y_AXIS);	// Box for player O check boxes
	Box labelBox = new Box(BoxLayout.Y_AXIS);
	Box buttonBox = new Box(BoxLayout.Y_AXIS);
	
	Square[][] box = new Square[9][9];		// 9 * 9 squares for the play board
	JLabel startLabel = new JLabel("  Select the player and start the game!  ");
    JLabel infoLabel = new JLabel(" ");
	// Check boxes for player X
	JLabel lP1 = new JLabel("Player X:");
	JCheckBox cbP11 = new JCheckBox("Player");
	JCheckBox cbP12 = new JCheckBox("Dumb AI");
	JCheckBox cbP13 = new JCheckBox("Random AI");
	JCheckBox cbP14 = new JCheckBox("Smart AI");
	// Check boxed for player O
	JLabel lP2 = new JLabel("Player O:");
	JCheckBox cbP21 = new JCheckBox("Player");
	JCheckBox cbP22 = new JCheckBox("Dumb AI");
	JCheckBox cbP23 = new JCheckBox("Random AI");
	JCheckBox cbP24 = new JCheckBox("Smart AI");
	
    JButton startButton = new JButton("Start");		// Start button
  
    MouseEvents me = new MouseEvents();
    
    public Board(Game g) {
    	
        
    	super("Five in a Row");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	this.game = g;
    	this.playerX = null;
    	this.playerO = null;
    	
    	g.addListener(this);
    	
    	// Create 9 * 9 squares
    	for (int i = 0; i < 9; ++ i){
    		row[i] = new Box(BoxLayout.X_AXIS);
    		for (int j = 0; j < 9; ++ j){
    			box[i][j] = new Square(i, j);
    			row[i].add(box[i][j]);
    			box[i][j].addMouseListener(me);
    			game.addListener(box[i][j]);
    		}
    		b.add(row[i]);
    	}
        
        Container cp = getContentPane();

        // Add all check boxes
        xBox.add(lP1);
        xBox.add(cbP11);
        xBox.add(cbP12);
        xBox.add(cbP13);
        xBox.add(cbP14);
        oBox.add(lP2);
        oBox.add(cbP21);
        oBox.add(cbP22);
        oBox.add(cbP23);
        oBox.add(cbP24);
        // Group the check boxes
        ButtonGroup group1 = new ButtonGroup();
        group1.add(cbP11);
        group1.add(cbP12);
        group1.add(cbP13);
        group1.add(cbP14);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(cbP21);
        group2.add(cbP22);
        group2.add(cbP23);
        group2.add(cbP24);
        
        // Add all components layer by layer
        checkBox.add(xBox);
        checkBox.add(oBox);
        startLabel.setAlignmentX(CENTER_ALIGNMENT);
        labelBox.add(startLabel);
        labelBox.add(Box.createVerticalGlue());
        infoLabel.setAlignmentX(CENTER_ALIGNMENT);
        labelBox.add(infoLabel);
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        buttonBox.add(startButton);
        controlBox.add(Box.createVerticalGlue());
        controlBox.add(labelBox);
        controlBox.add(Box.createVerticalGlue());
        controlBox.add(checkBox);
        controlBox.add(Box.createVerticalGlue());
        controlBox.add(buttonBox);
        controlBox.add(Box.createVerticalGlue());
        cp.add(b, BorderLayout.WEST);
        cp.add(controlBox, BorderLayout.EAST);
        
        cbP11.addActionListener(this);
        cbP12.addActionListener(this);
        cbP13.addActionListener(this);
        cbP14.addActionListener(this);
        cbP21.addActionListener(this);
        cbP22.addActionListener(this);
        cbP23.addActionListener(this);
        cbP24.addActionListener(this);
        startButton.addActionListener(this);
        
        pack(); 
        setVisible(true);
        setResizable(false);
       
    }
    
    public Square getSquare(int i, int j){
    	return box[i][j];
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	Object ob = e.getSource();
    	
    	// Clear the board
    	for(int i = 0; i < 9; ++i)
    		for(int j = 0; j < 9; ++j)	box[i][j].clear();
    	
    	// Start button clicked
    	if (ob instanceof JButton && playerX != null && playerO != null){	// Start the game
    		// Check if any of the players is not an AI
    		GUIController pX = null;
    		GUIController pO = null;
    		if (playerX instanceof GUIController) pX = (GUIController)playerX;
    		if (playerO instanceof GUIController) pO = (GUIController)playerO;
    		for (int i = 0; i < 9; ++ i){
    			for (int j = 0; j < 9; ++j) box[i][j].setPlayer(pX, pO);
    		}
    		game.addListener(playerX);
    		game.addListener(playerO);
    	}
    	// Check box clicked
    	if (ob instanceof JCheckBox){	// Create the controller for the game
    		if (ob == cbP11) playerX = new GUIController(Player.X, game);
    		if (ob == cbP12) playerX = new DumbAI(Player.X, this);
    		if (ob == cbP13) playerX = new RandomAI(Player.X, this);
    		if (ob == cbP14) playerX = new SmartAI(Player.X);
    		if (ob == cbP21) playerO = new GUIController(Player.O, game);
    		if (ob == cbP22) playerO = new DumbAI(Player.O, this);
    		if (ob == cbP23) playerO = new RandomAI(Player.O, this);
    		if (ob == cbP24) playerO = new SmartAI(Player.O);
    	}
    }

	@Override
	public void gameChanged(Game g) {	
		switch(g.getBoard().getState()) {
			case HAS_WINNER:
				infoLabel.setText(g.getBoard().getWinner().winner + " wins!");
				break;
			case DRAW:
				infoLabel.setText("Game ended in a draw!");
				break;
			case NOT_OVER:
				infoLabel.setText("It is player " + g.nextTurn() + "'s turn");
				break;
		}	
	}
}


