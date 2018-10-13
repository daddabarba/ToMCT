package ToMCT.View;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.View.GraphicComponents.ComponentsTypes.EmptyComponent;
import ToMCT.View.GraphicComponents.ComponentsTypes.GamePane;
import ToMCT.View.GraphicComponents.RelJComponent;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(String title, Game game){
        super(title);

        RelJComponent window = new EmptyComponent();
        new GamePane(window, game);

        add(window);

        //set preferences
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo (null);
        setVisible(true);

        //close program when no frames are opened
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
