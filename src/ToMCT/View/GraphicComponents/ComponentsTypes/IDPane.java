package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

public class IDPane extends RelJComponent{

    private int ID;

    public IDPane(RelJComponent parent, int ID, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        this.ID = ID;
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){

        AttributedString attID = new AttributedString("ID: " + ID);
        attID.addAttribute(TextAttribute.FONT, PlayerPane.FONT);

        //Print player ID
        g.drawString( attID.getIterator(), x, y);
    }
}
