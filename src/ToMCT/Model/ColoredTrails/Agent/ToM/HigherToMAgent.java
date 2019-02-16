package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.*;

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
        return goalBelief.getConfidence()*this.U(o, goal) + (1-goalBelief.getConfidence())*model.EV(o, player, opponent, goal);
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
        this.goalBelief = goalBeliefs.get(opponent);
    }

    // LEARNING

    public Belief update(Offer o, Player player, double lr){

        GoalBelief goalBelief = this.goalBeliefs.get(player);

        if(o.isAccept())
            return goalBelief;

        // copy goal beliefs
        double[][] updatedBeliefs = new double[goalBelief.getBeliefs().length][goalBelief.getBeliefs()[0].length];

        // update beliefs
        double sum = 0;

        for(int x=0; x<updatedBeliefs.length; x++){
            for(int y=0; y<updatedBeliefs.length; y++){
                double current = goalBelief.getBeliefs()[x][y]
                        *model.EV(
                                o.invert(),
                                player,
                                agent,
                                agent.getMap().getLocation(x,y)
                        )/model.EV(
                                (Offer)model.bestOffer(
                                        player,
                                        agent,
                                        agent.getMap().getLocation(x,y)
                                ).getKey(),
                                player,
                                agent,
                                agent.getMap().getLocation(x,y)
                        );

                sum+=current;
            }
        }

        final double finalSum = sum;

        for(int x=0; x<updatedBeliefs.length; x++) {
            final int finalX = x;
            Arrays.parallelSetAll(updatedBeliefs[x], i -> updatedBeliefs[finalX][i] / finalSum);
        }

        return new GoalBelief(updatedBeliefs);
    }

}
