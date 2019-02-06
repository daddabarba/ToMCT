package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Collection;
import java.util.HashMap;

public class ZeroToMAgent extends ToMAgent<ZeroBelief> {

    private HashMap<Player, ZeroBelief> zeroBeliefs;
    private ZeroBelief zeroBelief;

    // CONSTRUCTOR

    public ZeroToMAgent(Player agent, Collection<Player> players){
        super(0, agent, players);

        zeroBeliefs = new HashMap<>();
        zeroBeliefs.put(agent, new ZeroBelief());

        for(Player player : players)
            zeroBeliefs.put(player, new ZeroBelief());
    }

    // METHODS

    // POLICY

    public Offer ToM(Offer o, Player player, Player opponent, Location goal, ZeroBelief beliefs){

        ZeroBelief temp = zeroBeliefs.get(opponent);
        zeroBeliefs.put(opponent, beliefs);

        Offer ret = ToM(o, player, opponent, goal);

        zeroBeliefs.put(opponent, temp);
        return ret;

    }

    public double EV(Offer o, Player player, Player opponent, Location goal){
        double b = zeroBelief.get(o);

        double scoreOffer = agent.getScoreKeeper().score(o.getGot(), player.getPosition(), goal);
        double scoreHand = agent.getScoreKeeper().score(player.getHand(), player.getPosition(), goal);

        return b*scoreOffer + (1-b)*scoreHand;
    }

    @Override
    void setPlayer(Player player, Player opponent){

        super.setPlayer(player, opponent);

        this.player = player;
        this.opponent = opponent;
        this.zeroBelief = zeroBeliefs.get(opponent);
    }
}
