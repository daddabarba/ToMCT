package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Observable;
import java.util.Observer;

public class PlayerPostionPane extends RelJComponent implements Observer {

    private Player player;

    public PlayerPostionPane(RelJComponent parent, Player player, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        this.player = player;
        player.addObserver(this);
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){

        Location position = player.getPosition();

        //Print player location
        String coord = "@(" + position.getX() + "," + position.getY()+")";

        AttributedString attCoord = new AttributedString(coord);
        attCoord.addAttribute(TextAttribute.FONT, PlayerPane.FONT);

        g.drawString(attCoord.getIterator(), x, y);
    }

    @Override
    public void update(Observable observable, Object message){
        repaint();
    }
}
