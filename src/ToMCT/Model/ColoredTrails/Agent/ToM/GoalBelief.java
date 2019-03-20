package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

public class GoalBelief extends Belief{

    private double confidence;

    private void initPars(){
        confidence = 0.5;
    }

    public GoalBelief(int h, int w, double nGoals) {
        super(h, w, 1.0/nGoals);
        initPars();
    }

    public GoalBelief(double[][] beliefs, double confidence){
        super(beliefs);
        this.confidence = confidence;
    }

    // SETTERS

    public void init(Location l){
        this.init(l.getX(), l.getY());
    }

    public void put(Location l, double val){
        this.put(l.getX(), l.getY(), val);
    }

    public void reset(){
        for(int x=0; x<getBeliefs().length; x++)
            for(int y=0; y<getBeliefs()[0].length; y++)
                this.init(x,y);
    }

    // GETTERS

    public double get(Location l){
        return this.get(l.getX(), l.getY());
    }

    public double getConfidence(){
        return confidence;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.deleteCharAt(sb.length() - 1);

        sb.append(",\"confidence\":" + confidence + "}");

        return sb.toString();
    }
}
