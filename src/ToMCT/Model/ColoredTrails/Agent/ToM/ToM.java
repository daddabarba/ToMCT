package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public interface ToM {

    Offer ToM(Offer o, Player player);

    double EV(Offer o, Player player);
    double EV(Offer o);

    double U(Offer o, Player player);
    double U(Offer o);

    double EV(Offer o, Location l, Player player);
    double EV(Offer o, Location l);
}
