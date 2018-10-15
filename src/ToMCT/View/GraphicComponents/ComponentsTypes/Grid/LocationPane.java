package ToMCT.View.GraphicComponents.ComponentsTypes.Grid;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class LocationPane extends RelJComponent {

    public static int EDGE_THICKNESS = MapPane.EDGE_THICKNESS;
    public static Color EDGE_COLOR = MapPane.EDGE_COLOR;

    private Location location;

    public LocationPane(RelJComponent parent, Location location, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        this.location = location;
        new AvatarPane(this, location, 0.25, 0.25, 0.5, 0.5);
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
        g.drawRoundRect(x, y, width, height, 25 , 25);

        //Reset edge thickness
        if (oldStroke != null)
            ((Graphics2D) g).setStroke(oldStroke);

        //Paint filling

        //Set filling color
        g.setColor(location.getTrail().color());

        g.fillRoundRect(x, y, width, height,25,25);

        //Reset color
        g.setColor(oldColor);
    }
}
