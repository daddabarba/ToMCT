package ToMCT.Model.ColoredTrails.Agent.ToM;

import java.util.Collection;
import java.util.HashMap;

public abstract class Belief<T> {

    // FIELDS

    private HashMap<T,Double> pDist;

    // CONSTRUCTORS

    public Belief(Collection<T> ts){
        pDist = new HashMap<>();

        for(T t : ts)
            pDist.put(t, 0.0);
    }

    // SETTERS

    public void set(T t, Double val){
        if(val<0.0 || val>1.0)
            return;

        pDist.put(t, val);
    }

    // GETTERS

    public double get(T t){
        return pDist.get(t);
    }
}
