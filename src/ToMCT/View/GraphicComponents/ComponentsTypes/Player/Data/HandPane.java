package ToMCT.View.GraphicComponents.ComponentsTypes.Player.Data;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Hand;
import ToMCT.View.GraphicComponents.ComponentsTypes.Grid.MapPane;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class HandPane extends RelJComponent implements Observer {

    public static Color EDGE_COLOR = MapPane.EDGE_COLOR;

    private Hand hand;

    public HandPane(RelJComponent parent, Hand hand, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        hand.addObserver(this);
        this.hand = hand;
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){

        int offset = 0;
        int nChips = hand.getTotal();

        Color oldColor = g.getColor();

        //Print every chip
        for(Chip chip : hand.getChipCount().keySet()) {

            //Set frame color
            g.setColor(chip.trail().color());

            for (int i = 0; i < hand.getChipCount().get(chip); i++) {
                g.fillOval(x + offset, y, width/nChips, width/nChips);
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
