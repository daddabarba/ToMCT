package ToMCT;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.View.GameFrame;
import ToMCT.View.Menu;

public class Main {

    public static void main(String[] args){

        if(args.length < 6 )
            new Menu("Menu");
        else {

            Integer orders[] = {Integer.parseInt(args[0]), Integer.parseInt(args[1])};
            Double learningSpeeds[] = {Double.parseDouble(args[2]), Double.parseDouble(args[3])};

            int handSize = Integer.parseInt(args[4]);
            int mazeSize = Integer.parseInt(args[5]);

            boolean GUI = true;
            if (args.length > 6)
                GUI = Boolean.parseBoolean(args[6]);

            Game game = new Game(orders, learningSpeeds, handSize, mazeSize);

            game.startGame();

            if (GUI)
                new GameFrame("Colored Trails", game);
            else
                game.play();
        }
    }
}
