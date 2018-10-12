package ToMCT;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.Model.ColoredTrails.GameUtils.Location;

public class Main {

    public static void main(String[] args){

        int numPlayers = Integer.parseInt(args[0]);
        int handSize = Integer.parseInt(args[1]);

        int width = Integer.parseInt(args[2]);
        int height = Integer.parseInt(args[3]);

        Location start = new Location(Integer.parseInt(args[4]), Integer.parseInt(args[5]));

        Game game = new Game(numPlayers, handSize, width, height, start);
    }
}
