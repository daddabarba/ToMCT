package ToMCT.View;

import ToMCT.View.GraphicComponents.ComponentsTypes.EmptyComponent;
import ToMCT.View.GraphicComponents.ComponentsTypes.HierRectangle;
import ToMCT.View.GraphicComponents.RelJComponent;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(String title){
        super(title);

        RelJComponent total = new EmptyComponent();

        RelJComponent rec1 = new HierRectangle(total, 0.25,0.25,0.5,0.5);
        RelJComponent rec2 = new HierRectangle(rec1, 0.25,0.25,0.5,0.5);
        RelJComponent rec3 = new HierRectangle(rec2, 0.25,0.25,0.50,0.24);
        RelJComponent rec4 = new HierRectangle(rec2, 0.51,0.25,0.50,0.24);

        add(total);

        //set preferences
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo (null);
        setVisible(true);

        //close program when no frames are opened
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
