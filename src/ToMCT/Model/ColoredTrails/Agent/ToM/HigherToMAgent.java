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
        //goalBeliefs.put(agent, new GoalBelief(map.getHeight(), map.getWidth()));

        for(Player player : players)
            goalBeliefs.put(player, new GoalBelief(map.getHeight(), map.getWidth()));

    }

    // METHODS

    public Offer ToM(Offer o, Player player, Player opponent, Location goal, GoalBelief beliefs, boolean doLearn){

        GoalBelief temp = goalBeliefs.get(opponent);
        goalBeliefs.put(opponent, beliefs);

        Offer ret = ToM(o, player, opponent, goal, doLearn);

        goalBeliefs.put(opponent, temp);
        return ret;

    }

    public double EV(Offer o, Player player, Player opponent, Location goal){
        setPlayer(player, opponent);
        return goalBelief.getConfidence()*this.U(o, player, opponent, goal) + (1-goalBelief.getConfidence())*model.EV(o, player, opponent, goal);
    }


    private double U(Offer o, Player player, Player opponent, Location goal){

        double val = 0.0;

        Belief U = model.update(o, opponent, player);
        for(Location l : map.getGoals())
            val += this.goalBelief.get(l)*this.EV(o, player, opponent, goal, l, U);

        return val;
    }

    private double EV(Offer o, Player player, Player opponent, Location goal, Location l, Belief U){

        Offer predictedResponse = model.ToM(o, opponent, player, l, U, false);

        if(predictedResponse.isWithdraw())
            return agent.score(player.getHand(), player.getPosition(), goal);

        if(predictedResponse.isAccept())
            return agent.score(o.getGot(), player.getPosition(), goal);

        double scorePredictedOffer = agent.score(predictedResponse.getGiven(), player.getPosition(), goal);
        double scoreRefuse = agent.score(player.getHand(), player.getPosition(), goal);

        return Math.max(scorePredictedOffer, scoreRefuse);

    }

    @Override
    void setPlayer(Player player, Player opponent){
        this.goalBelief = goalBeliefs.get(opponent);
    }

    // LEARNING

    public Belief update(Offer o, Player player, Player opponent){

        GoalBelief goalBelief = this.goalBeliefs.get(opponent);

        if(o.isAccept())
            return goalBelief;

        // copy goal beliefs
        double[][] updatedBeliefs = new double[goalBelief.getBeliefs().length][goalBelief.getBeliefs()[0].length];

        // update beliefs
        double normalizingSum = 0;
        double totalSum = 0;

        double scoreOffer, scoreD0, current;
        
        for(int x=0; x<updatedBeliefs.length; x++){
            for(int y=0; y<updatedBeliefs[0].length; y++){

                Location goal = agent.getMap().getLocation(x,y);
                scoreOffer = agent.score(o.getGot(), opponent.getPosition(), goal);
                scoreD0 = agent.score(opponent.getHand(), opponent.getPosition(), goal);

                double update = goalBelief.getBeliefs()[x][y]
                        *((1 + model.EV(
                        o,
                        opponent,
                        player,
                        goal
                ))/(1 + (Double)model.bestOffer(
                        opponent,
                        player,
                        goal
                ).getValue()
                ));

                if(scoreOffer<=scoreD0)
                    current = 0;
                else
                    current = update;

                normalizingSum+=current;
                totalSum += update;
                updatedBeliefs[x][y] = current;
            }
        }

        final double finalSum = normalizingSum;

        if(finalSum > 0) {
            for (int x = 0; x < updatedBeliefs.length; x++) {
                final int finalX = x;
                Arrays.parallelSetAll(updatedBeliefs[x], i -> updatedBeliefs[finalX][i] / finalSum);
            }
        }

        double newConfidence = (1-this.learningSpeed)*goalBelief.getConfidence() + this.learningSpeed*totalSum;

        return new GoalBelief(updatedBeliefs, newConfidence);
    }

    public void finalizeUpdate(Offer o, Player player, Player opponent){
        this.goalBeliefs.put(opponent, (GoalBelief) this.update(o, player, opponent) );

        model.finalizeUpdate(o, player, opponent);

        o.invert();
        model.finalizeUpdate(o, opponent, player);
        o.invert();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"agent\": " + agent.getID());
        sb.append(", \"order\" : " + getOrder());
        sb.append(", \"learningSpeed\" : " + getLearningSpeed());
        sb.append(", \"beliefs\" : [");

        for(Map.Entry<Player, GoalBelief> e : goalBeliefs.entrySet())
            sb.append("{\"for\" : " + e.getKey().getID() + ", \"vals\": " + e.getValue().toString() + "},");
        sb.deleteCharAt(sb.length()-1);
        sb.append("], \"model\" : " + model.toString() + "}");

        return sb.toString();
    }

}
