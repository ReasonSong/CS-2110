import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import functional.Supplier;
import graph.GraphPanel;
import javax.swing.JSplitPane;

/** An instance is a JFrame with a bug tree and a panel to display information
 * @author Mshnik */
public class BugGUI extends JFrame {

    private static final long serialVersionUID = 1L; // Used for serializing

    /** Return a new BugFrame to display dt and s, the number of time steps to create it. */
    public static BugGUI show(BugTree dt, int s){
        return new BugGUI(dt, s);
    }

    /** Store the screen width and height of the current screen's size.
     * This method is called when the class is initially loaded */
    static {
        Dimension s= Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH= s.width;
        SCREEN_HEIGHT= s.height;
    }

    private static final int SCREEN_WIDTH;  // Width of the screen
    private static final int SCREEN_HEIGHT; // Height of the screen
    private static final int X_OFFSET= 50;  // Space between left of screen and window
    private static final int Y_OFFSET= 50;  // Space between top of screen and window
    private final int INFO_PANEL_MIN_WIDTH; // Minimum size of Info panel in windows
    private static final int WIDTH_COLOR_PANEL_HEIGHT= 38; // Height of color bar

    private static final Color DEAD_COLOR= Color.RED;  // Color of dead people in trees
    private static final Color IMMUNE_COLOR= new Color(14, 186, 11, 255);  // Color of immune people in trees

    private BugTree root;   // BugTree being displayed
    private int steps;      // Number of time steps it took to create tree root
    
    private final JSplitPane splitPane= new JSplitPane();  // Splitpane that takes up center space
    private GraphPanel<Human, HumanConnection> graphPanel;  // GraphPanel that shows the Bug

    private JLabel selectedDepthLabel= new JLabel(); // Display the width of the selected node
    private JLabel selectedWidthLabel= new JLabel(); // Display the width at the selected depth

    // Colored panel that highlights a particular depth level
    private ColoredPanel widthColorPanel= new ColoredPanel(new Color(63, 191, 191, 178));
    private JLabel depthHeaderLabel= new JLabel("  Depth"); // Label displaying the "Depth"
    private JLabel[] depthLabels; // Labels that count up the depth labels
    
    private JLabel framesLabel= new JLabel();  // Display number of frames in simulation

    private JLabel selectedHumanLabel= new JLabel();   // Display the selected human

    private JLabel selectedHumanParentLabel= new JLabel(); // Display parent of selected human

    // Display the number of children of the selected human
    private JLabel selectedHumanChildrenCountLabel= new JLabel();

    // Display the size of the subtree represented by the selected human
    private JLabel selectedHumanSubtreeCountLabel= new JLabel();

    // Display the maximum depth of the subtree represented by the selected human
    private JLabel selectedHumanMaxDepthLabel= new JLabel();

    // Display the maximum width of the subtree represented by the selected human
    private JLabel selectedHumanMaxWidthLabel= new JLabel();

    private JLabel twoHumanSelectedLabel= new JLabel(); // Display the previously selected human

    // Display the bug route from the previously selected human to the selected human
    private JLabel twoHumanBugRouteLabel= new JLabel();

    // Display the shared forebear of the previously selected and selected humans
    private JLabel sharedForebearLabel= new JLabel();

    private Human previouslySelectedHuman;  // The previously selected human
    private Human selectedHuman;  // The currently selected human

    // The circle that represents the currently selected human
    private GraphPanel<Human, HumanConnection>.Circle selectedCircle;

    // The bug route from the previously selected human to the currently selected human
    private List<Human> bugRoutePrevToSelected;

    /** Constructor: a visible GUI for dt, with s time steps needed to construct it. */
    private BugGUI(BugTree dt, int s){
        super();
        setTitle("A4 Bug Tree Explorer");

        root= dt;
        steps= s;
        getContentPane().add(splitPane, BorderLayout.CENTER);
        splitPane.setResizeWeight(1.0);

        fixGraphPanel(dt);
        splitPane.setRightComponent(graphPanel);//setLeftComponent
        //pack();
        
        JPanel infoPanel= new JPanel();
        fixInformationPanel(infoPanel);
        infoPanel.setPreferredSize(new Dimension(70, 0));
        splitPane.setLeftComponent(infoPanel); //setRightComponent
        //pack();
        
         
        // Double pack so that infoPanel.getWidth() can be used here
        pack();
        INFO_PANEL_MIN_WIDTH= infoPanel.getWidth();
        infoPanel.setMinimumSize(new Dimension(INFO_PANEL_MIN_WIDTH, 0));
        graphPanel.setPreferredSize(new Dimension(SCREEN_WIDTH - INFO_PANEL_MIN_WIDTH - X_OFFSET * 2, 
                SCREEN_HEIGHT - Y_OFFSET * 2));
        pack();
        setLocationRelativeTo(null);

        moveCirclesToCorrectLocations();

        splitPane.addPropertyChangeListener(new PropertyChangeListener() {
            /** Handle fixing layout when splitpane line moves */
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt != null  &&  evt.getPropertyName().equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
                    widthColorPanel.setSize(new Dimension(graphPanel.getWidth(), widthColorPanel.getHeight()));
                    layoutNodes();
                }
            }
        });
        createAndAddCircleMouseListener();

        // Apply colors
        colorNodes(dt);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    /** Create and add a circle mouse listener for selecting humans to the graphPanel */
    private void createAndAddCircleMouseListener() {
        graphPanel.addCircleMouseListener(new MouseListener() {
            @SuppressWarnings("unchecked")
            /** Handle change of info when a circle is clicked */
            public void mouseClicked(MouseEvent e) {
                selectedCircle= (GraphPanel<Human, HumanConnection>.Circle) e.getSource();

                Human p= selectedCircle.getRepresents();
                previouslySelectedHuman= selectedHuman;
                selectedHuman= p;

                try{
                    bugRoutePrevToSelected= root.getTree(previouslySelectedHuman).bugRouteTo(selectedHuman);
                } catch(Exception excp) {}

                widthColorPanel.setLocation(0, selectedCircle.getY1() - widthColorPanel.getHeight() / 2);

                populateLabels();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }
    
    /** Move circles on the graphPanel to correct locations. */
    public void moveCirclesToCorrectLocations() {
        // Move circles to correct locations
        widthColorPanel.setSize(new Dimension(graphPanel.getWidth(), WIDTH_COLOR_PANEL_HEIGHT));
        widthColorPanel.setLocation(0, -WIDTH_COLOR_PANEL_HEIGHT);
        layoutNodes();
        addComponentListener(new ComponentListener() {
            /** Handle fixing layout when the window is resized */
            public void componentResized(ComponentEvent e) {
                widthColorPanel.setSize(new Dimension(graphPanel.getWidth(), widthColorPanel.getHeight()));
                layoutNodes();

                if (selectedCircle != null) {
                    widthColorPanel.setLocation(0, selectedCircle.getY1() - widthColorPanel.getHeight() / 2);
                }
            }
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });
        
    }

    /** Store in field graphPanel the graph for dt. */
    private void fixGraphPanel(BugTree dt) {
        graphPanel= new GraphPanel<>(new Network(dt));
        graphPanel.setLinesBendable(false);
        graphPanel.setCirclesDraggable(false);
        graphPanel.setCircleStringToDrawFunc((c) -> c.getRepresents().getName());
        graphPanel.setLineStringToDrawFunc((l) -> "");
        graphPanel.setLineStrokeFunc(this::getStrokeFor);

        graphPanel.add(widthColorPanel);

        try {
            int depth= dt.maxDepth();
            depthLabels= new JLabel[depth + 1];
            for (int i= 0; i <= depth; i= i+1) {
                depthLabels[i]= new JLabel(i + "");
                depthLabels[i].setSize(new Dimension(50,50));
                graphPanel.add(depthLabels[i]);
                graphPanel.setComponentZOrder(depthLabels[i], 0);
            }
            graphPanel.add(depthHeaderLabel);
            depthHeaderLabel.setSize(new Dimension(50,20));
            depthHeaderLabel.setLocation(20, 2);
        } catch (Exception e) {
            depthLabels= null;
        }
    }

    /** Place in infoPanel all the labels that give information about the selected human(s) */
    private void fixInformationPanel(JPanel infoPanel) {
        infoPanel.setBackground(new Color(216, 255, 235));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        infoPanel.setBackground(new Color(216, 255, 235));
        infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
        
        infoPanel.add(new JLabel("                                                             "));
        infoPanel.add(framesLabel);
        infoPanel.add(new JLabel("                                                             "));
        infoPanel.add(selectedDepthLabel);
        infoPanel.add(selectedWidthLabel);
        infoPanel.add(new JLabel("  "));
        infoPanel.add(selectedHumanLabel);
        infoPanel.add(selectedHumanParentLabel);
        infoPanel.add(selectedHumanChildrenCountLabel);
        infoPanel.add(selectedHumanSubtreeCountLabel);
        infoPanel.add(selectedHumanMaxDepthLabel);
        infoPanel.add(selectedHumanMaxWidthLabel);
        infoPanel.add(new JLabel("  "));
        infoPanel.add(twoHumanSelectedLabel);
        infoPanel.add(twoHumanBugRouteLabel);
        infoPanel.add(sharedForebearLabel);
    }

    /** Lay out the nodes in the BugTree.
     * Specifically, call recLayoutNodes(...) [below], with particular starting
     * arguments, and set the location of all of the depth labels. */
    private void layoutNodes() {
        float yPercent= 0.05f;
        float yInc= 1.0f / (float)(root.maxDepth() + 1);

        recLayoutNodes(root, 0.025f, 0.975f, yPercent, yInc);
        if (depthLabels != null) {
            for (int i= 0; i < depthLabels.length; i++) {
                depthLabels[i].setLocation(20, (int) (yPercent * graphPanel.getHeight()) - 30 );
                yPercent += yInc;
            }
        }
    };

    /** Lay out the nodes of dt on the GraphPanel.<br>
     * xMin is the minimum percentage of the width this layout is allowed to take.<br>
     * xMax is the maximum percentage of the width this layout is allowed to take.<br>
     * yPercent is the current height percentage for this recursive call.<br>
     * yInc is the amount to increase yPercent in each recursive call.<br>
     * Precondition: 0 <= xMin <= yMax <= 1. */
    private void recLayoutNodes(BugTree dt, float xMin, float xMax, float yPercent, float yInc) {
        float xCenter= (xMin + xMax) / 2.0f;
        GraphPanel<Human, HumanConnection>.Circle c= graphPanel.getCircle(dt.getRoot());

        c.setX1((int) ((float)graphPanel.getWidth() * xCenter));
        c.setY1((int) ((float)graphPanel.getHeight() * yPercent));

        float maxWidthTotals= 0.0f;
        for (BugTree child : dt.copyOfChildren()) {
            maxWidthTotals += child.maxWidth();
        }

        float xInc= (xMax - xMin) / maxWidthTotals;
        yPercent += yInc;
        for (BugTree child : dt.copyOfChildren()) {
            float width= (float) child.maxWidth();
            recLayoutNodes(child, xMin, xMin + xInc * width, yPercent, yInc);
            xMin += xInc * width;
        }
    }

    /** Return a stroke (for drawing) for line l.
     * If the line is part of the bugRoute, return a bolded stroke;
     * otherwise, a simple stroke. */
    private Stroke getStrokeFor(GraphPanel<Human, HumanConnection>.Line l) {
        if (bugRoutePrevToSelected != null) {
            Human p1= l.getC1().getRepresents();
            Human p2= l.getC2().getRepresents();
            if (bugRoutePrevToSelected.contains(p1)  &&  bugRoutePrevToSelected.contains(p2)) {
                return new BasicStroke(3.0f);
            }
        }
        return new BasicStroke();
    }

    /** Color the nodes of dt. <br>
     * Nodes for dead humans are colored DEAD_COLOR.
     * Nodes for immune humans are colored IMMUNE_COLOR */
    private void colorNodes(BugTree dt) {
        GraphPanel<Human, HumanConnection>.Circle c= graphPanel.getCircle(dt.getRoot());
        c.setColor(dt.getRoot().isDead() ? DEAD_COLOR : IMMUNE_COLOR);
        for (BugTree child : dt.copyOfChildren()) {
            colorNodes(child);
        }
    }

    /** Repopulate the information labels that have been added to the infoPanel.
     * Uses lambdas - not covered in CS2110, you'll learn about them in CS3110 */
    private void populateLabels() {
        framesLabel.setText("No. steps in simulation: " + steps);
        
        Human p= selectedHuman;
        populateLabel(() -> root.depthOf(p), selectedDepthLabel, "Selected human depth");
        populateLabel(() -> root.widthAtDepth(root.depthOf(p)), selectedWidthLabel, "Selected level width");
        populateLabel(() -> p.getName(), selectedHumanLabel, "Selected human");

        populateLabel(() -> {
            Human parent= root.getParent(p);
            if (parent != null) return parent.getName();
            else return "null";
        }, selectedHumanParentLabel, "Parent");

        populateLabel(() -> root.getTree(p).numberOfChildren(), selectedHumanChildrenCountLabel, "Children");
        populateLabel(() -> root.getTree(p).size(), selectedHumanSubtreeCountLabel, "Subtree size");
        populateLabel(() -> root.getTree(p).maxDepth(), selectedHumanMaxDepthLabel, "Subtree depth");
        populateLabel(() -> root.getTree(p).maxWidth(), selectedHumanMaxWidthLabel, "Subtree width");

        Human pp= previouslySelectedHuman;
        populateLabel(() -> (pp != null ? pp.getName() : "null") +
                " and " + p.getName(), twoHumanSelectedLabel, "Selected humans");
        if (pp != null) {
            populateLabel(() -> {
                List<Human> route= root.getTree(pp).bugRouteTo(p);
                if (route == null) return null;
                else return Arrays.deepToString(route.stream().map(a -> a.getName()).toArray());
            }, twoHumanBugRouteLabel, "Bug route");
            populateLabel(() -> {
                Human parent= root.sharedForebearOf(pp, p);
                return parent == null ? "null" : parent.getName();
            }, sharedForebearLabel, "Shared forbear");
        }
    }

    /** Call f and put its output into l.<br>
     * If f returns, set l to text catenated with the output of the call.<br>
     * If the call results in an exception, set the text of l to
     * text and the type of exception instead. 
     * Uses lambdas - not covered in CS2110, you'll learn about them in CS3110 */
    private static void populateLabel(Supplier<?> f, JLabel l, String text) {
        final String INDENT= "   ";
        try {
            l.setText(INDENT + text + ": " + f.apply() + INDENT);
        } catch (Throwable t) {
            l.setText(INDENT + text + ": " + "Exception occurred - " + t.toString() + INDENT);
        }
    }

    /** An instance is a simple colored rectangle that is used to highlight 
     * a particular depth level in the tree.
     * @author Patashnik  */
    @SuppressWarnings("serial")
    private static class ColoredPanel extends JPanel{
        private Color color;

        /** Constructor: a new ColoredPanel with the specified color. */
        private ColoredPanel(Color c) {
            color= c;
        }

        /** Paint this ColoredPanel by drawing a rectangle of its color,
         * filling the whole area of the Panel. */
        public @Override void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
