package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;

import java.util.Collection;
import java.util.HashMap;

public class ConfidenceBelief extends HashMap<Player, Double> {

    // FIELDS
    private double def;

    // CONSTRUCTORS

    public ConfidenceBelief(){
        super();
        def = 0.0;
    }

    public ConfidenceBelief(Collection<Player> players){
        this();

        for(Player player : players)
            init(player);
    }

    // SETTERS

    public void init(Player p){
        this.put(p, this.def);
    }

    @Override
    public Double put(Player p, Double val){
        if(val<0.0 || val>1.0)
            return null;

        return super.put(p, val);
    }

    public ConfidenceBelief setDef(double val){
        this.def = val;
        return this;
    }

    // GETTERS

    @Override
    public Double get(Object o){

        Player p;

        try {
            p = (Player) o;
        }catch (ClassCastException e){
            return null;
        }

        if(!containsKey(p))
            init(p);

        return super.get(p);
    }
}
