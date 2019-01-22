package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.Model.ColoredTrails.GameUtils.ScoreKeeper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ToMAgent<T extends Belief> implements ToM<T>{

    // FIELDS

    Player agent;

    // parameters

    private HashMap<Player, ConfidenceBelief> confidences;
    protected double confidence;

    // state

    Player player;
    Player opponent;

    // CONSTRUCTOR

    public ToMAgent(Player agent, Collection<Player> players){
        this.agent = agent;

        confidences = new HashMap<>();

        for(Player player : players)
            confidences.put(player, (ConfidenceBelief)(new ConfidenceBelief(players)).setDef(0.5));
    }

    // METHODS

    public Offer ToM(Offer o, Player player, Player opponent, Location goal){
        setPlayer(player, opponent);

        Offer bestOffer = bestOffer(player, opponent, goal);

        double valBest = EV(bestOffer, player, opponent, goal);
        double valOffer = agent.getScoreKeeper().score(o.getGiven(), player.getPosition(), goal);
        double valHand = agent.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        if(valBest>valOffer && valBest>valHand)
            return bestOffer;

        if(valOffer>=valBest && valOffer>valHand)
            return new Offer(o, Offer.Intention.ACCEPT);

        return new Offer(o, Offer.Intention.WITHDRAW);
    }

    public Offer bestOffer(Player player, Player opponent, Location goal){

        Iterator<Offer> offerIterator = Offer.getIterator(player, opponent);

        double maxVal = -1;
        Offer bestOffer = null;

        while(offerIterator.hasNext()){

            Offer currentOffer = offerIterator.next();
            double offerVal = EV(currentOffer, player, opponent, goal);

            if(maxVal<0 || offerVal>maxVal){
                maxVal = offerVal;
                bestOffer = currentOffer;
            }
        }

        return bestOffer;

    }

    // SETTERS

    void setPlayer(Player player, Player opponent){
        confidence = confidences.get(player).get(opponent);
    }
}
