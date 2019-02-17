package ToMCT.Model.ColoredTrails.GameUtils;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;

public interface TimeKeeper {

    void timeHasPassed(Player player1, Player player2, Offer o);
}
