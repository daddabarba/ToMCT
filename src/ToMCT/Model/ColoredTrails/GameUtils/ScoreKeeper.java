package ToMCT.Model.ColoredTrails.GameUtils;

import ToMCT.Model.ColoredTrails.GameTools.Chips.Hand;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public interface ScoreKeeper {

    double score(Hand hand, Location start, Location goal);

}
