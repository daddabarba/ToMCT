package ToMCT.View;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.View.GraphicComponents.ComponentsTypes.EmptyComponent;
import ToMCT.View.GraphicComponents.ComponentsTypes.GamePane;
import ToMCT.View.GraphicComponents.RelJComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameFrame extends JFrame {

    private class ActionPlay extends AbstractAction{

        private Game game;
        public ActionPlay(String name, Game game){
            super(name);
            this.game = game;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            game.play();
        }

    }

    private class GetData extends AbstractAction{

        private Game game;
        public GetData(String name, Game game){
            super(name);
            this.game = game;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println(game.toString());
        }
    }

    public GameFrame(String title, Game game){
        super(title);

        RelJComponent window = new EmptyComponent();
        new GamePane(window, game);

        JMenuBar menuBar = new JMenuBar();

        JButton playButton = new JButton("Play");
        playButton.setAction(new ActionPlay("Play", game));

        JButton getDataButton = new JButton("Get Data");
        getDataButton.setAction(new GetData("Get Data", game));

        menuBar.add(playButton);
        menuBar.add(getDataButton);

        setJMenuBar(menuBar);
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
