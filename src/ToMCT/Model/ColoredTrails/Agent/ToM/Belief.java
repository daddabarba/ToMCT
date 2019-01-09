package ToMCT.Model.ColoredTrails.Agent.ToM;

import java.util.Collection;
import java.util.HashMap;

public abstract class Belief<T> {

    // FIELDS

    private HashMap<T,Double> pDist;

    // CONSTRUCTORS

    public Belief(){
        pDist = new HashMap<>();
    }

    public Belief(Collection<T> ts){
        this();

        for(T t : ts)
            init(t);
    }

    // SETTERS

    public void init(T t){
        pDist.put(t, 0.0);
    }

    public void set(T t, Double val){
        if(val<0.0 || val>1.0)
            return;

        pDist.put(t, val);
    }

    // GETTERS

    public double get(T t){
        if(!pDist.containsKey(t))
            init(t);

        return pDist.get(t);
    }
}
