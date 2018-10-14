package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class PlayerPane extends RelJComponent {

    public static int EDGE_THICKNESS = 2;
    public static Color EDGE_COLOR = Color.BLACK;

    public static Color BACKGROUND_COLOR = MapPane.EDGE_COLOR;

    public static String FONT_STYLE = "Verdana";
    public static int FONT_SIZE = 18;
    public static Font FONT = new Font(FONT_STYLE, Font.PLAIN, FONT_SIZE);

    public PlayerPane(RelJComponent parent, Player player, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        new IDPane(this, player.getID(), 0.02, 0.3, 1, 0.2);
        new PlayerPostionPane(this, player, 0.2,0.3,1,0.2);
        new HandPane(this, player.getHand(), 0.4, 0.1, 1, 0.6);
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){

        //Set frame color
        Color oldColor = g.getColor();
        g.setColor(BACKGROUND_COLOR);

        //Draw frame rectangle
        g.fillRoundRect(x,y,width,height,10,10);

        //Set edge thickness
        Stroke oldStroke = null;
        if (g instanceof Graphics2D) {
            oldStroke = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(EDGE_THICKNESS));
        }

        //Set frame color
        g.setColor(EDGE_COLOR);

        //Draw frame rectangle
        g.drawRoundRect(x,y,width,height,10,10);

        //Reset color
        g.setColor(oldColor);

        //Reset edge thickness
        if (oldStroke != null)
            ((Graphics2D) g).setStroke(oldStroke);
    }
}
