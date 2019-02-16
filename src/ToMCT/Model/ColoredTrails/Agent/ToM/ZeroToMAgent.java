package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class ZeroToMAgent extends ToMAgent<ZeroBelief> {

    private HashMap<Player, ZeroBelief> zeroBeliefs;
    private ZeroBelief zeroBelief;

    // CONSTRUCTOR

    public ZeroToMAgent(Player agent, double learningSpeed, Collection<Player> players){
        super(0, learningSpeed, agent, players);

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
        this.zeroBelief = zeroBeliefs.get(opponent);
    }

    // LEARNING

    public Belief update(Offer o, Player player){

        ZeroBelief zeroBelief = this.zeroBeliefs.get(player);

        if(o.isAccept())
            return zeroBelief;

        // copy current beliefs
        double[][] uBeliefs = new double[zeroBelief.getBeliefs().length][zeroBelief.getBeliefs()[0].length];
        for(int i=0; i<zeroBelief.getBeliefs().length; i++)
            System.arraycopy(zeroBelief.getBeliefs()[i], 0, uBeliefs[i], 0, zeroBelief.getBeliefs()[i].length);

        // compute starting m
        int numNeg = ZeroBelief.abstractOffer(o)%10;

        // update all affected values
        for(int i=((o.isWithdraw()) ? (numNeg) : (numNeg+1)); i<uBeliefs.length; i++) {
            double factor = Math.pow(1-this.learningSpeed, i);
            int ip = i;
            Arrays.parallelSetAll(uBeliefs[i], k -> factor*zeroBelief.getBeliefs()[ip][k]);
        }

        return new ZeroBelief(uBeliefs);
    }


    public void finalizeUpdate(Offer o, Player player){
        this.zeroBeliefs.put(player, (ZeroBelief) this.update(o, player));
    }
    
}
