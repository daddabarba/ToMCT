package ToMCT.View.GraphicComponents.ComponentsTypes.Player.Data;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.HandUtils;
import ToMCT.View.GraphicComponents.ComponentsTypes.Grid.MapPane;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class HandPane extends RelJComponent implements Observer {

    public static Color EDGE_COLOR = MapPane.EDGE_COLOR;

    private Player player;

    public HandPane(RelJComponent parent, Player player, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        player.addObserver(this);
        this.player = player;
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){

        int hand = player.getHand();

        int offset = 0;
        int nChips = HandUtils.getTotal(hand);

        Color oldColor = g.getColor();

        //Print every chip
        for(Chip chip : Chip.values()) {

            //Set frame color
            g.setColor(chip.trail().color());

            for (int i = 0; i < HandUtils.getChipCount(hand, chip); i++) {
                g.fillOval(x + offset, y, width/nChips, (int)(height*0.8));
                offset += width/nChips;
            }
        }

        g.setColor(oldColor);
    }

    @Override
    public void update(Observable observable, Object message){
        repaint();
    }

}
