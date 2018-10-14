package ToMCT.View.GraphicComponents.ComponentsTypes.Player;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.View.GraphicComponents.RelJComponent;

import java.awt.*;
import java.util.Collection;

public class PlayerListPane extends RelJComponent {

    public PlayerListPane(RelJComponent parent, Collection<Player> players, double x, double y, double height, double width){
        super(parent, x, y, height, width);

        double offset = 0.0;
        int nPlayers = players.size();

        for(Player player : players){
            new PlayerPane(this, player, 0, offset, height/nPlayers, 1);
            offset += height/nPlayers;
        }
    }

    @Override
    public void PaintAbsElement(Graphics g, int x, int y, int height, int width){
        //Print nothing
        return;
    }
}
