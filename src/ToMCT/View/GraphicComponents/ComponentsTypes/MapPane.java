package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.GameTools.Map;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class MapPane extends RelJComponent {

    public static int EDGE_THICKNESS = GamePane.EDGE_THICKNESS;
    public static Color EDGE_COLOR = GamePane.EDGE_COLOR;

    public MapPane(RelJComponent parent, Map map, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        for(int r=0; r<map.getHeight(); r++) {
            for (int c = 0; c < map.getWidth(); c++) {

                double locX = ((double) c)/map.getWidth();
                double locY = ((double) r)/map.getHeight();
                double locW = ((double) 1)/map.getWidth();
                double locH = ((double) 1)/map.getHeight();

                new LocationPane(this, map.getLocations()[r][c], locX, locY, locH, locW);
            }
        }
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

        //reset edge thickness
        if(oldStroke!=null)
            ((Graphics2D) g).setStroke(oldStroke);

        //Reset color
        g.setColor(oldColor);
    }
}
