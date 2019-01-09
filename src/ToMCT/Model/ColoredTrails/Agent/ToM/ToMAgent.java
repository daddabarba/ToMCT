package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.Model.ColoredTrails.GameUtils.ScoreKeeper;
import sun.java2d.opengl.OGLContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ToMAgent implements ToM{

    // FIELDS

    Player agent;
    Location goal;

    // parameters

    private double confidence = 0.5;

    // state

    Player player;
    Player opponent;

    // CONSTRUCTOR

    public ToMAgent(Player agent, Location goal){
        this.goal = goal;
        this.agent = agent;
    }

    // METHODS

    public Offer ToM(Offer o, Player player, Player opponent){
        setPlayer(player, opponent);

        Offer bestOffer = bestOffer(player, opponent);

        double valBest = EV(bestOffer, player, opponent);
        double valOffer = agent.getScoreKeeper().score(o.getGiven(), player.getPosition(), goal);
        double valHand = agent.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        if(valBest>valOffer && valBest>valHand)
            return bestOffer;

        if(valOffer>=valBest && valOffer>valHand)
            return new Offer(o, Offer.Intention.ACCEPT);

        return new Offer(o, Offer.Intention.WITHDRAW);
    }

    public Offer bestOffer(Player player, Player opponent){

        Iterator<Offer> offerIterator = Offer.getIterator(player, opponent);

        double maxVal = -1;
        Offer bestOffer = null;

        while(offerIterator.hasNext()){

            Offer currentOffer = offerIterator.next();
            double offerVal = EV(currentOffer, player, opponent);

            if(maxVal<0 || offerVal>maxVal){
                maxVal = offerVal;
                bestOffer = currentOffer;
            }
        }

        return bestOffer;

    }

    // SETTERS

    abstract void setPlayer(Player player, Player opponent);

    // GETTERS
    public double getConfidence(){
        return confidence;
    }

}
