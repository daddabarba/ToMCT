package ToMCT.Model.ColoredTrails.GameUtils;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public interface ScoreKeeper {

    double score(int hand, Location start, Location goal);

}
