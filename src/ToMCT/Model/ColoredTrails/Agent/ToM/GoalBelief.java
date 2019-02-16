package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public class GoalBelief extends Belief{

    private double confidence;

    private void initPars(){
        confidence = 0.5;
    }

    public GoalBelief(int h, int w) {
        super(h, w);
        initPars();
    }

    public GoalBelief(double[][] beliefs){
        super(beliefs);
        initPars();
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

    public double getConfidence(){
        return confidence;
    }
}
