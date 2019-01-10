package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public interface ToM {

    Offer ToM(Offer o, Player player, Player opponent, Location goal);
    Offer bestOffer(Player player, Player opponent, Location goal);

    double EV(Offer o, Player player, Player opponent, Location goal);
}
