package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;

public class ZeroBelief extends Belief{

    private double[][] beliefs;
    private double def;

    public ZeroBelief() {
        super(Chip.values().length+1, Chip.values().length+1);
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

    // SETTERS

    public void init(Offer o){
        int abstr = abstractOffer(o);
        this.init(abstr/10, abstr%10);
    }

    public void put(Offer o, double val){
        int abstr = abstractOffer(o);
        this.put(abstr/10, abstr%10, val);
    }

    // GETTERS

    public double get(Offer o){
        int abstr = abstractOffer(o);
        return this.get(abstr/10, abstr%10);
    }
}
