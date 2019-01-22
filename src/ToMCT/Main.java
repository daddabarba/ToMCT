package ToMCT;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.View.GameFrame;
import ToMCT.View.Menu;

public class Main {

    public static void main(String[] args){
        new Menu("Menu");
        /*int numPlayers = Integer.parseInt(args[0]);
        int handSize = Integer.parseInt(args[1]);

        int width = Integer.parseInt(args[2]);
        int height = Integer.parseInt(args[3]);

        Location start = new Location(Integer.parseInt(args[4]), Integer.parseInt(args[5]));

        boolean GUI = true;
        if(args.length>6)
            GUI = Boolean.parseBoolean(args[6]);

        Game game = new Game(numPlayers, handSize, width, height, start);
        game.startGame();

        if(GUI)
            new GameFrame("Colored Trails", game);*/
    }
}
