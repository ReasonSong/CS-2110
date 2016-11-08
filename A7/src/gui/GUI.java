package gui;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import graph.*;
import student.Paths;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/** An instance is the JFrame that shows the graph.
 * Class graph.Main and perhaps other classes send updates to the gui
 * to keep it up to date on the graph state.
 * The user has no interaction with class GUI. */
public class GUI extends JFrame{

    private static final long serialVersionUID = 2941318999657277463L;

    public static final int X_OFFSET = 100;
    public static final int Y_OFFSET = 50;

    public static final int DRAWING_BOARD_WIDTH_MIN = 400;
    public static final int DRAWING_BOARD_HEIGHT_MIN = 400;

    public static final int DRAWING_BOARD_WIDTH;	//Default
    public static final int DRAWING_BOARD_HEIGHT; 	//Default

    public static final int UPDATE_PANEL_HEIGHT= 100;
    public static final int SIDE_PANEL_WIDTH= 300;

    static {
        Dimension s= Toolkit.getDefaultToolkit().getScreenSize();
        DRAWING_BOARD_WIDTH= (int)(s.width) - SIDE_PANEL_WIDTH - 2 * X_OFFSET;
        DRAWING_BOARD_HEIGHT= (int)(s.height * 0.8) - UPDATE_PANEL_HEIGHT - 2 * Y_OFFSET;
    }

    private int drawingBoardWidth;	//Most recent value of width
    private int drawingBoardHeight; //Most recent value of height

    private GUI self;			//A reference to this, for use in anonymous inner classes
    private Graph board;       // The board this gui draws
    private boolean initialized;	//True once the initial construction process is done, false until then

    private JPanel drawingPanel; // The main panel on which the board is drawn
    private JPanel sidePanel;	 // The info panel located on the right of the board.

    private JMenuBar menuBar;    //The menu bar at the top of the gui
    
    private String startClick= "Click a start node";
    private String endClick= "Click an end node";
    
    private JLabel spInfo= new JLabel(""); // Information displayed to user
    private Node startNode= null; // Selected start node for shortest path (null if none)
    private Node endNode= null; // Selected end node for shortest path (null if none)
    private List<Node> path; // the path highlighted in red on GUI (null if none)
    
    private JButton spButton= new JButton(startClick);

    /** Set either the start node or the end node to n, depending on the state,
     * and change state. If end node, call shortest path algorithm and display
     * shortest path on GUI */
    public void setNode(Node n) {
        if (spButton.getText().equals(startClick)) {
            if (path != null) {
                setColors(path, Color.black);
                path= null;
            }
            startNode= n;
            spInfo.setText("Start node is " + startNode);
            spButton.setText(endClick);
            return;
        }
        
        if (spButton.getText().startsWith(endClick)) {
            endNode= n;
            spInfo.setText("Start node is " + startNode + ", end node is " + endNode + 
                    ". Calculating shortest path.");
            path= Paths.shortestPath(startNode, endNode);
            int len= setColors(path, Color.red);
            spInfo.setText("Path " + startNode + " to " + endNode + 
                    " has " + path.size() + " nodes and length " + len);
            spButton.setText(startClick);
            return;
        }
    }
    
    /** Set the color of all edges in path to c, repaint if changes, and
     * return the path length. */
    public int setColors(List<Node> path, Color c) {
        Iterator<Node> iter= path.iterator();
        if (!iter.hasNext()) return 0;
        
        int length= 0;
        Node beg= iter.next();
        while (iter.hasNext()) {
            Node end= iter.next();
            Edge e= beg.getEdge(end);
            length= length + e.length;
            Line line= e.getLine();
            line.setColor(c);
            beg= end;
        }
        repaint();
        return length;
    }

    /** Constructor: a window to show a board b. */
    public GUI(Graph b) {
        board= b;
        self= this;

        setMinimumSize(new Dimension(SIDE_PANEL_WIDTH + DRAWING_BOARD_WIDTH_MIN,
                UPDATE_PANEL_HEIGHT + DRAWING_BOARD_HEIGHT_MIN));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        fixDrawingPanel();
        fixSidePanel();
        fixBottomPanel(); 
        fixMenuBar();
        
        spButton.setEnabled(false);

        pack();
        validate();
        repaint();
        drawingBoardHeight= drawingPanel.getHeight();
        drawingBoardWidth= drawingPanel.getWidth();
        setBoard(board);
        initialized= true;
        setLocation(X_OFFSET, Y_OFFSET);
        drawingPanelResized();
        setVisible(true);
    }
    
    /** fix the drawing panel --field drawingPanel */
    public void fixDrawingPanel() {
        drawingPanel= new JPanel();
        drawingPanel.setBorder(new LineBorder(new Color(131,155,255)));
        drawingPanel.setBackground(Color.WHITE);

        drawingBoardWidth= DRAWING_BOARD_WIDTH;
        drawingBoardHeight= DRAWING_BOARD_HEIGHT;

        drawingPanel.setPreferredSize(new Dimension(drawingBoardWidth, drawingBoardHeight));
        drawingPanel.setLayout(null);
        drawingPanel.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                drawingPanelResized();
            }
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });

        getContentPane().add(drawingPanel, BorderLayout.CENTER);
    }
    
    /** fix the side panel --field sidePanel */
    public void fixSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setBorder(new LineBorder(new Color(131,155,255)));
        sidePanel.setBackground(new Color(203, 255, 181));
        sidePanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH,
                DRAWING_BOARD_HEIGHT + UPDATE_PANEL_HEIGHT));
        sidePanel.setLayout(new BorderLayout());

        getContentPane().add(sidePanel, BorderLayout.EAST);
    }
    
    /** Fix bottom panel */
    public void fixBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(DRAWING_BOARD_WIDTH, UPDATE_PANEL_HEIGHT));
        bottomPanel.setBackground(new Color(181,255,252));
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel updatePanel = new JPanel();
        updatePanel.setBackground(bottomPanel.getBackground());
        bottomPanel.add(updatePanel);
        
        updatePanel.add(spInfo);
        updatePanel.add(new JLabel("   "));
        updatePanel.add(spButton);

        JLabel lblSpace = new JLabel("\t\t");
        bottomPanel.add(lblSpace);
    }
    

    
    /** Fix top menu, field menuBar */
    public void fixMenuBar() {
        menuBar= new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile= new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmQuit = new JMenuItem("Quit");
        mntmQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = JOptionPane.showConfirmDialog(null, 
                        "Are You Sure You Want to Quit?");
                if (returnVal == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        mnFile.add(mntmQuit);

        JMenu mnGame= new JMenu("Graph");
        menuBar.add(mnGame);

        JMenuItem mntmRandom= new JMenuItem("New Random Map...");
        mntmRandom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                long returnVal= -1;
                String s = "";
                while (returnVal == -1 && s != null) {
                    try{
                        s= JOptionPane.showInputDialog(null, "Enter seed for random graph (any long)");
                        returnVal= Long.parseLong(s);
                    } catch(NumberFormatException e) {
                    }
                }
                if (s == null) {
                    return;
                }
                board= Graph.randomBoard(returnVal);
                setBoard(board);
                startNode= null;
                endNode= null;
                spInfo.setText("");
                spButton.setText(startClick);
            }
        });
        mnGame.add(mntmRandom);

        JMenuItem mntmPrintJSON= new JMenuItem("Print Game JSON");
        mntmPrintJSON.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(board.toJSONString());
            }
        });
        mnGame.add(mntmPrintJSON);
    }

    /** Call to show the message for a json parsing error */
    private void showJSONParseError(JSONException j, File fil){
        String msg= "Err with reading board " + fil.getName() + " : " 
                + j.getMessage() + "\n" +
                "Try pasting the contents of " + fil.getName() + 
                " into a JSON validator online.\n"
                + "Ex: jsonlint.com";
        JOptionPane.showMessageDialog(self, msg);
    }

    /** Resize the drawing panel.
     *  Called internally when the drawing panel is resized */
    private void drawingPanelResized() {
        if (!initialized) return;

        Dimension newSize= drawingPanel.getSize();
        double heightRatio= (double)newSize.height / (double)drawingBoardHeight;
        double widthRatio= (double)newSize.width / (double)drawingBoardWidth;

        for (Node n : board.getNodes()) {
            Circle c= n.getCircle();
            n.updateGUILocation((int)Math.round((c.getX1() * widthRatio)), 
                    (int)Math.round((c.getY1() * heightRatio)));
        }

        drawingBoardWidth= newSize.width;
        drawingBoardHeight= newSize.height;
    }
    
    /** Set the board to b and redraw the map. */
    public void setBoard(Graph b) {
        drawingPanel.removeAll();
        board= b;
        board.setGUI(this);
        board.updateMinMaxLength();
        drawMap();
        

        Dimension newSize= drawingPanel.getSize();
        double heightRatio= (double)newSize.height / (double)DRAWING_BOARD_HEIGHT;
        double widthRatio= (double)newSize.width / (double)DRAWING_BOARD_WIDTH;
                
        for (Node n : board.getNodes()) {
            Circle c= n.getCircle();
            n.updateGUILocation((int)Math.round((c.getX1() * widthRatio)), 
                    (int)Math.round((c.getY1() * heightRatio)));
        }
        
        updateSidePanel();
        validate();
        repaint();
    }

    /** Draw all elements of the board in the drawingPanel. Called as part of
     * GUI construction and whenever a new board is loaded. */
    private void drawMap() {
        //Put nodes on map
        for (Node n : board.getNodes()) {
            Circle c= n.getCircle();
            //Remove and re-add from drawing panel
            drawingPanel.remove(c);
            drawingPanel.add(c);
        }

        //Draw the edges on the map
        for (Edge r : board.getEdges()) {
            Line l= r.getLine();
            l.setC1(r.getExits()[0].getCircle());
            l.setC2(r.getExits()[1].getCircle());
            l.setBounds(drawingPanel.getBounds());
            drawingPanel.remove(l);
            drawingPanel.add(l);
        }

        //Fix the z-ordering of elements on the panel
        //Higher z painted first -> lower z paint over higher z
        int z = 0;
        for (Node n : board.getNodes()) {
            drawingPanel.setComponentZOrder(n.getCircle(), z);
            z++;
        }
        for (Edge e : board.getEdges()) {
            drawingPanel.setComponentZOrder(e.getLine(), z);
            z++;
        }
        repaint();
    }

    /** Update the info panel to the new game that was just loaded. */
    private void updateSidePanel() {
        sidePanel.removeAll();

        JLabel gameLabel = null;
        gameLabel= new JLabel("Map from Seed: " +  board.getSeed());
        gameLabel.setFont(Font.decode("asdf-14"));
        sidePanel.add(gameLabel, BorderLayout.NORTH);
        
        JLabel citiesLabel= new JLabel("Number of cities: " + board.getNodesSize());
        sidePanel.add(citiesLabel, BorderLayout.CENTER);

    }

    /** Recursively pad zeroes on left such that the resulting string has digits characters */
    private static String fixNumber(int x, int digits, String s){
        int a= 0;
        if (x == 1){
            a= 1;
        } else {
            a= (int)Math.ceil(Math.log10(x));
        }
        if (digits == a) return s + x;
        return fixNumber(x, digits-1, s + "0");
    }

    /** Return the panel on which the map is drawn. */
    public JPanel getDrawingPanel() {
        return drawingPanel;
    }
}
