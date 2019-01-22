package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Hand;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;

import java.util.Collection;
import java.util.Map;

public class ZeroBelief extends Belief<ZeroBelief.AbstractOffer> {

    public class AbstractOffer{

        private Integer numPos, numNeg;

        public AbstractOffer(Offer offer){

            numPos = 0;
            numNeg = 0;

            Hand got = offer.getGot();
            Hand given = offer.getGiven();

            for(Map.Entry<Chip, Integer> e : given.getChipCount().entrySet())
                if(e.getValue() > got.getChipCount(e.getKey()))
                    numPos += 1;
                else
                    numNeg +=1;
        }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof AbstractOffer))
                return false;

            return  (((AbstractOffer)o).getNumNeg().equals(numNeg)) && (((AbstractOffer)o).getNumPos().equals(numPos));
        }

        public Integer getNumPos(){
            return numPos;
        }

        public Integer getNumNeg(){
            return numNeg;
        }
    }

    public ZeroBelief(){
        super();
    }

    public ZeroBelief(Collection<AbstractOffer> offers){
        super(offers);
    }

    // SETTERS

    public void init(Offer o){
        this.init(new AbstractOffer(o));
    }

    public Double put(Offer o, Double val){
        return this.put(new AbstractOffer(o), val);
    }

    // GETTERS

    public Double get(Offer o){
        return this.get(new AbstractOffer(o));
    }
}
