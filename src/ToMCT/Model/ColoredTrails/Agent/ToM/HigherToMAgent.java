package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HigherToMAgent extends ToMAgent {

    private ToM model;

    private HashMap<Player, GoalBelief> goalBeliefs;

    private GoalBelief goalBelief;

    // CONSTRUCTOR
    public HigherToMAgent(Player agent, int order, Collection<Player> players, Collection<Location> locations, Location goal){

        super(agent, goal);

        if(order>1)
            model = new HigherToMAgent(player, order-1, players, locations, goal);
        else
            model = new ZeroToMAgent(agent, players, goal);

        goalBeliefs = new HashMap<>();
        goalBeliefs.put(agent, new GoalBelief(locations));

        for(Player player : players)
            goalBeliefs.put(player, new GoalBelief(locations));

    }

    // METHODS

    public double EV(Offer o, Player player, Player opponent){
        setPlayer(player, opponent);
        return this.EV(o);
    }

    private double EV(Offer o){
        return getConfidence()*this.U(o) + (1-getConfidence())*model.EV(o, player, opponent);
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
            return agent.getScoreKeeper().score(player.getHand(), agent.getPosition(), goal);

        if(predictedResponse.isAccept())
            return agent.getScoreKeeper().score(o.getGot(), agent.getPosition(), goal);

        double scorePredictedOffer = agent.getScoreKeeper().score(predictedResponse.getGiven(), agent.getPosition(), goal);
        double scoreRefuse = agent.getScoreKeeper().score(player.getHand(), agent.getPosition(), goal);

        return Math.max(scorePredictedOffer, scoreRefuse);

    }

    @Override
    void setPlayer(Player player, Player opponent){
        this.player = player;
        this.opponent = opponent;
        this.goalBelief = goalBeliefs.get(opponent);
    }

}
