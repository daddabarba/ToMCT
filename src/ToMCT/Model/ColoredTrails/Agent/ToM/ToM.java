package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public interface ToM {

    Offer ToM(Offer o, Player player, Player opponent);

    Offer bestOffer(Player player, Player opponent);

    double EV(Offer o, Player player, Player opponent);
    //double EV(Offer o);

    double U(Offer o, Player player, Player opponent);
    //double U(Offer o);

    double EV(Offer o, Location l, Player player, Player opponent);
    //double EV(Offer o, Location l);
}
