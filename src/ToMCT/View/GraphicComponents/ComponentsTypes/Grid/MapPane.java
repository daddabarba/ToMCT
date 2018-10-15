package ToMCT.View.GraphicComponents.ComponentsTypes.Grid;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Map;
import ToMCT.View.GraphicComponents.ComponentsTypes.GamePane;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class MapPane extends RelJComponent {

    public static int EDGE_THICKNESS = GamePane.EDGE_THICKNESS;
    public static Color EDGE_COLOR = Color.LIGHT_GRAY;

    public static double PADDING = -0.05;

    public MapPane(RelJComponent parent, Map map, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        for(int r=0; r<map.getHeight(); r++) {
            for (int c = 0; c < map.getWidth(); c++) {

                double locX = ((double) c)/map.getWidth();
                double locY = ((double) r)/map.getHeight();
                double locW = ((double) 1)/map.getWidth() + PADDING;
                double locH = ((double) 1)/map.getHeight() + PADDING;

                new LocationPane(this, map.getLocations()[r][c], locX, locY, locH, locW);
            }
        }
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){
        //Paint nothing
        return;
    }
}
