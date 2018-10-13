package ToMCT.View.GraphicComponents.ComponentsTypes;

import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;

public class EmptyComponent extends RelJComponent {
    //Empty frame (useful as parent node of the components hierarchy in the view)

    public EmptyComponent(){
        //No parent, therefore it's height and width are the screen's
        super(0,0);
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){
        //Print nothing
        return;
    }
}
