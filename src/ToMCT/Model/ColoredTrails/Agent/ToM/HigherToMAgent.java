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
    private ToMCT.Model.ColoredTrails.GameTools.Grid.Map map;

    // CONSTRUCTOR
    public HigherToMAgent(Player agent, int order, double learningSpeed, Collection<Player> players, ToMCT.Model.ColoredTrails.GameTools.Grid.Map map){

        super(order, learningSpeed, agent, players);
        this.map = map;

        if(order>1)
            model = new HigherToMAgent(agent, order-1, learningSpeed, players, map);
        else
            model = new ZeroToMAgent(agent, learningSpeed, players);

        goalBeliefs = new HashMap<>();
        goalBeliefs.put(agent, new GoalBelief(map.getHeight(), map.getWidth()));

        for(Player player : players)
            goalBeliefs.put(player, new GoalBelief(map.getHeight(), map.getWidth()));

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

        for(Location l : map.getGoals())
            val += this.goalBelief.get(l)*this.EV(o,goal, l);

        return val;
    }

    private double EV(Offer o, Location goal, Location l){

        Offer predictedResponse = model.ToM(o.invert(), opponent, player, l);

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
