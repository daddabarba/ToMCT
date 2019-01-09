package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Collection;

public class GoalBelief extends Belief<Location>{

    public GoalBelief(Collection<Location> locations){
        super(locations);
    }
}
