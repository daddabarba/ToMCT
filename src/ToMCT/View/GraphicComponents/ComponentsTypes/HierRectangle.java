package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class HierRectangle extends RelJComponent {

    public HierRectangle(RelJComponent parent, double x, double y, double height, double width){
        super(parent, x, y, height, width);
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){
        g.drawRect(x, y, width, height);
    }
}
