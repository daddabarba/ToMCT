package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;

import java.util.Arrays;

public class ZeroBelief extends Belief{

    public ZeroBelief() {
        super(Chip.values().length+1, Chip.values().length+1);
    }

    public ZeroBelief(double[][] beliefs){
        super(beliefs);
    }

    // OFFER ABSTRACTION

    public static int abstractOffer(Offer offer){

        int numPos = 0;
        int numNeg = 0;

        int got = offer.getGot();
        int given = offer.getGiven();

        for(int i=0; i<Chip.values().length; i++) {
            if (given % 10 > got % 10)
                numPos += 1;
            else
                numNeg += 1;

            got/=10;
            given/=10;
        }

        return numNeg + 10*numPos;
    }

    // BELIEF UPDATE

    public ZeroBelief update(Offer o, double lr){

        if(o.isAccept())
            return this;

        // copy current beliefs
        double[][] uBeliefs = new double[beliefs.length][beliefs[0].length];
        for(int i=0; i<beliefs.length; i++)
            System.arraycopy(beliefs[i], 0, uBeliefs[i], 0, beliefs[i].length);

        // compute starting m
        int numNeg = abstractOffer(o)%10;

        // update all affected values
        for(int i=((o.isWithdraw()) ? (numNeg) : (numNeg+1)); i<uBeliefs.length; i++) {
            double factor = Math.pow(1-lr, i);
            int ip = i;
            Arrays.parallelSetAll(uBeliefs[i], k -> factor*beliefs[ip][k]);
        }

        return new ZeroBelief(uBeliefs);
    }

    // SETTERS

    public void init(Offer o){
        int abstr = abstractOffer(o);
        this.init(abstr%10, abstr/10);
    }

    public void put(Offer o, double val){
        int abstr = abstractOffer(o);
        this.put(abstr%10, abstr/10, val);
    }

    // GETTERS

    public double get(Offer o){
        int abstr = abstractOffer(o);
        return this.get(abstr%10, abstr/10);
    }
}
