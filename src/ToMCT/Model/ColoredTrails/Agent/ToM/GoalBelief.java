package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public class GoalBelief extends Belief{

    private double[][] beliefs;
    private double def;

    public GoalBelief(int h, int w) {
        super(h, w);
    }

    // SETTERS

    public void init(Location l){
        this.init(l.getX(), l.getY());
    }

    public void put(Location l, double val){
        this.put(l.getX(), l.getY(), val);
    }

    // GETTERS

    public double get(Location l){
        return this.get(l.getX(), l.getY());
    }
}
