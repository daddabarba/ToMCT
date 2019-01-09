package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ToMAgent implements ToM{

    // FIELDS

    private Player agent;

    private ToM model;
    private HashMap<Player, GoalBelief> goalBeliefs;

    // parameters

    private double confidence = 0.5;

    // state

    private GoalBelief goalBelief;
    private Player player;

    // CONSTRUCTOR

    public ToMAgent(Player agent, int order, Collection<Player> players, Collection<Location> locations){

        this.agent = agent;

        if(order>1)
            model = new ToMAgent(player, order-1, players, locations);
        else
            model = new ZeroToMAgent();

        goalBeliefs = new HashMap<>();
        goalBeliefs.put(player, new GoalBelief(locations));

        for(Player player : players)
            goalBeliefs.put(player, new GoalBelief(locations));

    }

    public Offer ToM(Offer o, Player player){
        setPlayer(player);
        return null;
    }

    public double EV(Offer o, Player player){
        setPlayer(player);
        return this.EV(o);
    }

    public double EV(Offer o){
        return confidence*this.U(o) + (1-confidence)*model.U(o, player);
    }

    public double U(Offer o, Player player){
        setPlayer(player);
        return this.U(o);
    }

    public double U(Offer o){

        double val = 0.0;
        Iterator it = goalBelief.getPDist().entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<Location, Double> entry = (Map.Entry)it.next();
            val += entry.getValue()*this.EV(o,entry.getKey());
        }

        return val;
    }

    public  double EV(Offer o, Location l, Player player){
        setPlayer(player);
        return this.EV(o,l,player);
    }

    public double EV(Offer o, Location l){
        return 0.0;
    }

    // SETTERS

    private void setPlayer(Player player){
        this.player = player;
        this.goalBelief = goalBeliefs.get(player);
    }

}
