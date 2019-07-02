package ToMCT;

import ToMCT.Model.ColoredTrails.Game;
import ToMCT.View.GameFrame;
import ToMCT.View.Menu;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

// Input format:
// 0: order_agent_1
// 1: order_agent_2
// 2: ls_agent_1
// 3: ls_agent_2
// 4: hand_size
// 5: maze_size
// (6: num_games)
// (7: GUI)
// (8: output_file_name)

public class Main {

    public static void main(String[] args){

        if(args.length < 6 )
            new Menu("Menu");
        else {

            Integer orders[] = {Integer.parseInt(args[0]), Integer.parseInt(args[1])};
            Double learningSpeeds[] = {Double.parseDouble(args[2]), Double.parseDouble(args[3])};

            for (int i=0; i<2; i+=1)
                while(learningSpeeds[i]>1.0)
                    learningSpeeds[i]/=10;

            int handSize = Integer.parseInt(args[4]);
            int mazeSize = Integer.parseInt(args[5]);

            boolean GUI = true;
            if (args.length > 7)
                GUI = Boolean.parseBoolean(args[7]);

            int times = 1;
            if (args.length > 6)
                times = Integer.parseInt(args[6]);

            Game game = new Game(orders, learningSpeeds, handSize, mazeSize);

            game.startGame();
	    game.play(times);
            System.out.println(game.toString());
        }
    }
}
