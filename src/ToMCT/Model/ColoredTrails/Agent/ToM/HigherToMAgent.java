package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HigherToMAgent extends ToMAgent<GoalBelief> {

    private ToM model;

    private HashMap<Player, GoalBelief> goalBeliefs;

    private GoalBelief goalBelief;

    // CONSTRUCTOR
    public HigherToMAgent(Player agent, int order, Collection<Player> players, Collection<Location> locations){

        super(agent, players);

        if(order>1)
            model = new HigherToMAgent(player, order-1, players, locations);
        else
            model = new ZeroToMAgent(agent, players);

        goalBeliefs = new HashMap<>();
        goalBeliefs.put(agent, new GoalBelief(locations));

        for(Player player : players)
            goalBeliefs.put(player, new GoalBelief(locations));

    }

    // METHODS

    public Offer ToM(Offer o, Player player, Player opponent, Location goal, GoalBelief beliefs){

        GoalBelief temp = goalBeliefs.get(opponent);
        goalBeliefs.put(opponent, beliefs);

        Offer ret = ToM(o, player, opponent, goal);

        goalBeliefs.put(opponent, temp);
        return ret;

    }

    public double EV(Offer o, Player player, Player opponent, Location goal){
        setPlayer(player, opponent);
        return this.EV(o, goal);
    }

    private double EV(Offer o, Location goal){
        return confidence*this.U(o, goal) + (1-confidence)*model.EV(o, player, opponent, goal);
    }


    private double U(Offer o, Location goal){

        double val = 0.0;
        Iterator it = goalBelief.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Location, Double> entry = (Map.Entry)it.next();
            val += entry.getValue()*this.EV(o,goal, entry.getKey());
        }

        return val;
    }

    private double EV(Offer o, Location goal, Location l){

        Offer predictedResponse = model.ToM(o, opponent, player, l);

        if(predictedResponse.isWithdraw())
            return agent.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        if(predictedResponse.isAccept())
            return agent.getScoreKeeper().score(o.getGot(), player.getPosition(), goal);

        double scorePredictedOffer = agent.getScoreKeeper().score(predictedResponse.getGiven(), player.getPosition(), goal);
        double scoreRefuse = agent.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        return Math.max(scorePredictedOffer, scoreRefuse);

    }

    @Override
    void setPlayer(Player player, Player opponent){

        super.setPlayer(player, opponent);

        this.player = player;
        this.opponent = opponent;
        this.goalBelief = goalBeliefs.get(opponent);
    }

}
