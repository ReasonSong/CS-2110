package gui;

import java.awt.Point;
import java.awt.event.*;

import graph.Graph;
import graph.GraphElement;
import graph.Node;

/** An instance is a circle that can be dragged by the user on the gui */
public class DraggableCircle extends Circle {

    private static final long serialVersionUID = -3983152780751574074L;
    private Point clickPoint; //The point the user clicked within the circle before dragging began
    private int maxX;   //Boundary for dragging on the x
    private int maxY;   //Boundary for dragging on the y


    /** Constructor: an instance at (x, y) of diameter d that represents piece r. */
    public DraggableCircle(final GraphElement r, int x, int y, int d) {
        super(r, x, y, d);

        MouseListener clickListener= new MouseListener(){

            /** When clicked, store the initial point at which this is clicked. */
            @Override public void mousePressed(MouseEvent e) {
                maxX= r.getGraph().gui.getDrawingPanel().getWidth();
                maxY= r.getGraph().gui.getDrawingPanel().getHeight();
                clickPoint= e.getPoint();
            }

            /** This is the selection of a start node or end node for shortest path algorithm */
            @Override public void mouseClicked(MouseEvent e) {
                Graph bd= represents.getGraph();
                bd.gui.setNode((Node) represents);
            }

            @Override public void mouseReleased(MouseEvent e) {}

            @Override public void mouseEntered(MouseEvent e) {}

            @Override public void mouseExited(MouseEvent e) {}	
        };

        MouseMotionListener motionListener = new MouseMotionListener(){

            /** When this is dragged, perform the translation from the point
             * where this was clicked to the new dragged point. */
            public @Override void mouseDragged(MouseEvent e) {
                DraggableCircle c= (DraggableCircle)e.getSource();
                Point p= e.getPoint();
                int newX= Math.min(maxX, Math.max(0, c.getX1() + p.x - clickPoint.x));
                int newY= Math.min(maxY, Math.max(0, c.getY1() + p.y - clickPoint.y));
                c.represents.updateGUILocation(newX, newY);
            }

            @Override public void mouseMoved(MouseEvent e) {}
        };

        addMouseListener(clickListener);
        addMouseMotionListener(motionListener);
    }

}
