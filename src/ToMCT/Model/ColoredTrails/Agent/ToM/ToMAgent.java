package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.Model.ColoredTrails.GameUtils.ScoreKeeper;

import java.util.*;

public abstract class ToMAgent<T extends Belief> implements ToM<T>{

    // FIELDS

    Player agent;

    // parameters

    double learningSpeed;

    private int order;

    // CONSTRUCTOR

    public ToMAgent(int order, double learningSpeed, Player agent, Collection<Player> players){
        this.agent = agent;
        this.order = order;

        this.learningSpeed = learningSpeed;
    }

    // METHODS

    public Offer ToM(Offer o, Player player, Player opponent, Location goal){
        return ToM(o, player, opponent, goal, true);
    }

    public Offer ToM(Offer o, Player player, Player opponent, Location goal, boolean doLearn){

        this.setPlayer(player, opponent);

        if(doLearn)
            this.finalizeUpdate(o, player, opponent);

        Map.Entry<Offer, Double> bo = bestOffer(player, opponent, goal);
        Offer bestOffer = bo.getKey();

        double valBest = bo.getValue();
        double valOffer = agent.score(o.getGiven(), player.getPosition(), goal);
        double valHand = agent.score(player.getHand(), player.getPosition(), goal);

        Offer inverse = new Offer(o.invert());
        o.invert();

        if(valBest>valOffer && valBest>valHand)
            return bestOffer;

        if(valOffer>=valBest && valOffer>valHand)
            return new Offer(inverse, Offer.Intention.ACCEPT);

        return new Offer(inverse, Offer.Intention.WITHDRAW);
    }

    public Map.Entry<Offer, Double> bestOffer(Player player, Player opponent, Location goal){

        this.setPlayer(player, opponent);
        
        Iterator<Offer> offerIterator = Offer.getIterator(player, opponent);

        double maxVal = -1;
        Offer bestOffer = null;

        while(offerIterator.hasNext()){

            Offer currentOffer = offerIterator.next();
            double offerVal = EV(currentOffer, player, opponent, goal);

            if(maxVal<0 || offerVal>maxVal){
                maxVal = offerVal;
                bestOffer = new Offer(currentOffer);
            }
        }

        return new AbstractMap.SimpleEntry<Offer, Double>(bestOffer, maxVal);

    }

    // SETTERS

    abstract void setPlayer(Player player, Player opponent);

    @Override
    public void resetBeliefs(){
        return;
    }

    // GETTERS

    public double getLearningSpeed(){
        return learningSpeed;
    }

    public int getOrder(){
        return order;
    }

    @Override
    public abstract String toString();
}
