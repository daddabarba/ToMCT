package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameUtils.Location;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Observable;
import java.util.Observer;

public class LocationPane extends RelJComponent implements Observer {

    public static int EDGE_THICKNESS = MapPane.EDGE_THICKNESS;
    public static Color EDGE_COLOR = MapPane.EDGE_COLOR;

    public static Color PLAYER_COLOR = Color.DARK_GRAY;

    private Location location;

    public LocationPane(RelJComponent parent, Location location, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        location.addObserver(this);
        this.location = location;
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width) {
        //Make frame of map

        //Paint frame

        //Set edge thickness
        Stroke oldStroke = null;
        if (g instanceof Graphics2D) {
            oldStroke = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(EDGE_THICKNESS));
        }

        //Set frame color
        Color oldColor = g.getColor();
        g.setColor(EDGE_COLOR);

        //Draw frame
        g.drawRect(x, y, width, height);

        //Reset edge thickness
        if (oldStroke != null)
            ((Graphics2D) g).setStroke(oldStroke);

        //Paint filling

        //Set filling color
        g.setColor(location.getTrail().color());

        g.fillRect(x, y, width, height);

        //Paint player

        if(location.getPlayers().size()>0) {
            //Set player color
            g.setColor(PLAYER_COLOR);

            //Paint circle for player
            g.fillOval(x+width/4, y+height/4, width/2, height/2);
        }

        //Reset color
        g.setColor(oldColor);
    }

    @Override
    public void update(Observable observable, Object message){
        repaint();
    }
}
