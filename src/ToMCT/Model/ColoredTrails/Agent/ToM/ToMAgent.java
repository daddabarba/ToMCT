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

public class ToMAgent implements ToM{

    // FIELDS

    private Player agent;

    private ToM model;
    private HashMap<Player, GoalBelief> goalBeliefs;

    private Location goal;

    // parameters

    private double confidence = 0.5;

    // state

    private GoalBelief goalBelief;
    private Player player;
    private Player opponent;

    // CONSTRUCTOR

    public ToMAgent(Player agent, int order, Collection<Player> players, Collection<Location> locations, Location goal){

        this.goal = goal;
        this.agent = agent;

        if(order>1)
            model = new ToMAgent(player, order-1, players, locations, goal);
        else
            model = new ZeroToMAgent();

        goalBeliefs = new HashMap<>();
        goalBeliefs.put(player, new GoalBelief(locations));

        for(Player player : players)
            goalBeliefs.put(player, new GoalBelief(locations));

    }

    public Offer ToM(Offer o, Player player, Player opponent){
        setPlayer(player, opponent);

        Offer bestOffer = bestOffer(player, opponent);

        double valBest = EV(bestOffer);
        double valOffer = player.getScoreKeeper().score(o.getGiven(), player.getPosition(), goal);
        double valHand = player.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

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

    public double EV(Offer o, Player player, Player opponent){
        setPlayer(player, opponent);
        return this.EV(o);
    }

    private double EV(Offer o){
        return confidence*this.U(o) + (1-confidence)*model.EV(o, player, opponent);
    }

    public double U(Offer o, Player player, Player opponent){
        setPlayer(player, opponent);
        return this.U(o);
    }

    private double U(Offer o){

        double val = 0.0;
        Iterator it = goalBelief.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Location, Double> entry = (Map.Entry)it.next();
            val += entry.getValue()*this.EV(o,entry.getKey());
        }

        return val;
    }

    public  double EV(Offer o, Location l, Player player, Player opponent){
        setPlayer(player, opponent);
        return this.EV(o,l);
    }

    private double EV(Offer o, Location l){

        Offer predictedResponse = model.ToM(o, opponent, player);

        if(predictedResponse.isWithdraw())
            return player.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        if(predictedResponse.isAccept())
            return player.getScoreKeeper().score(o.getGot(), player.getPosition(), goal);

        double scorePredictedOffer = player.getScoreKeeper().score(predictedResponse.getGiven(), player.getPosition(), goal);
        double scoreRefuse = player.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        return Math.max(scorePredictedOffer, scoreRefuse);

    }

    // SETTERS

    private void setPlayer(Player player, Player opponent){
        this.player = player;
        this.opponent = opponent;
        this.goalBelief = goalBeliefs.get(opponent);
    }

}
