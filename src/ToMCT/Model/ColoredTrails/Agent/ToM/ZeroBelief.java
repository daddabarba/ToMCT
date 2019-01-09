package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;

import java.util.Collection;

public class ZeroBelief extends Belief<Offer> {

    public ZeroBelief(){
        super();
    }

    public ZeroBelief(Collection<Offer> offers){
        super(offers);
    }
}
