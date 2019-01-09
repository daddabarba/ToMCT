package ToMCT.Model.ColoredTrails.Agent.ToM;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public abstract class Belief<T> extends HashMap<T, Double> {

    // CONSTRUCTORS

    public Belief(){
        super();
    }

    public Belief(Collection<T> ts){
        this();

        for(T t : ts)
            init(t);
    }

    // SETTERS

    public void init(T t){
        this.put(t, 0.0);
    }

    @Override
    public Double put(T t, Double val){
        if(val<0.0 || val>1.0)
            return null;

        return super.put(t, val);
    }

    // GETTERS

    @Override
    public Double get(Object o){

        T t;

        try {
            t = (T) o;
        }catch (ClassCastException e){
            return null;
        }

        if(!containsKey(t))
            init(t);

        return super.get(t);
    }
}
