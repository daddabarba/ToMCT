package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.GameUtils.Location;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class LocationPane extends RelJComponent implements Observer {

    public static int EDGE_THICKNESS = MapPane.EDGE_THICKNESS;
    public static Color EDGE_COLOR = MapPane.EDGE_COLOR;

    private Location location;

    public LocationPane(RelJComponent parent, Location location, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        location.addObserver(this);
        this.location = location;
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){
        //Make frame of map

        //Set edge thickness
        Stroke oldStroke = null;
        if(g instanceof Graphics2D) {
            oldStroke =((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(EDGE_THICKNESS));
        }

        //Set color
        Color oldColor = g.getColor();
        g.setColor(EDGE_COLOR);

        //Draw frame
        g.drawRect(x, y, width, height);

        //Reset edge thickness
        if(oldStroke!=null)
            ((Graphics2D) g).setStroke(oldStroke);

        //Set color
        g.setColor(location.getTrail().color());

        g.fillRect(x, y, width, height);

        //Reset color
        g.setColor(oldColor);
    }

    @Override
    public void update(Observable observable, Object message){
        repaint();
    }
}