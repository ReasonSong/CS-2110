package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import model.Game;
import model.GameListener;

public class Board extends JFrame implements ActionListener, GameListener{

	Box  b = new Box(BoxLayout.Y_AXIS);		// Box for the whole GUI
	Box[] row = new Box[9];					// Box for each row of the play board
	Box controlBox = new Box(BoxLayout.Y_AXIS);	// Box for all the control components on the right side
	Box checkBox = new Box(BoxLayout.X_AXIS);	// Box for all check boxes
	Box xBox = new Box(BoxLayout.Y_AXIS);	// Box for player X check boxes
	Box oBox = new Box(BoxLayout.Y_AXIS);	// Box for player O check boxes
	Box labelBox = new Box(BoxLayout.Y_AXIS);
	Box buttonBox = new Box(BoxLayout.Y_AXIS);
	
	Square[][] box = new Square[9][9];		// 9 * 9 squares for the play board
    JLabel infoLabel = new JLabel("Ready to start");
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
    
    public Board() {
        
    	super("Five in a Row");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	for (int i = 0; i < 9; ++ i){
    		row[i] = new Box(BoxLayout.X_AXIS);
    		for (int j = 0; j < 9; ++ j){
    			box[i][j] = new Square(i, j);
    			box[i][j].addMouseListener(me);
    			row[i].add(box[i][j]);
    		}
    		b.add(row[i]);
    	}
        
        Container cp = getContentPane();

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
        
        startButton.addActionListener(this);
        
        pack(); 
        setVisible(true);
        setResizable(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	for (int i = 0; i < 9; ++ i){
    		for (int j = 0; j < 9; ++j) box[i][j].clear();
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


