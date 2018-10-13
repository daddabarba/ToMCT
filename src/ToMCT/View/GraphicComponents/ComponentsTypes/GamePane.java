package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class GamePane extends RelJComponent {

    public static Color BACKGROUND_COLOR = Color.GRAY;

    public static int EDGE_THICKNESS = 10;
    public static Color EDGE_COLOR = GamePane.BACKGROUND_COLOR;

    public GamePane(RelJComponent parent, Game game){
        super(parent, 0, 0, 1, 1);

        //Generate sub components
        new MapPane(this, game.getMap(), 0.25, 0.25, 0.5, 0.5);
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){
        //Print background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(x,y,width,height);
    }
}
