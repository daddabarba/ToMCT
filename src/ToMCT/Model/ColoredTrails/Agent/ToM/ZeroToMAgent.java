package ToMCT.Model.ColoredTrails.Agent.ToM;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ZeroToMAgent extends ToMAgent<ZeroBelief> {

    private HashMap<Player, ZeroBelief> zeroBeliefs;
    private ZeroBelief zeroBelief;

    // CONSTRUCTOR

    public ZeroToMAgent(Player agent, double learningSpeed, Collection<Player> players){
        super(0, learningSpeed, agent, players);

        zeroBeliefs = new HashMap<>();
        //zeroBeliefs.put(agent, new ZeroBelief());

        for(Player player : players)
            zeroBeliefs.put(player, new ZeroBelief());
    }

    // METHODS

    // POLICY

    public Offer ToM(Offer o, Player player, Player opponent, Location goal, ZeroBelief beliefs, boolean doLearn){

        ZeroBelief temp = zeroBeliefs.get(opponent);
        zeroBeliefs.put(opponent, beliefs);

        Offer ret = ToM(o, player, opponent, goal, doLearn);

        zeroBeliefs.put(opponent, temp);
        return ret;

    }

    public double EV(Offer o, Player player, Player opponent, Location goal){

        setPlayer(player, opponent);

        double b = zeroBelief.get(o);

        double scoreOffer = agent.score(o.getGot(), player.getPosition(), goal);
        double scoreHand = agent.score(player.getHand(), player.getPosition(), goal);

        return b*scoreOffer + (1-b)*scoreHand;
    }

    @Override
    void setPlayer(Player player, Player opponent){
        this.zeroBelief = zeroBeliefs.get(opponent);
    }

    // LEARNING

    public Belief update(Offer o, Player player, Player opponent){

        ZeroBelief zeroBelief = this.zeroBeliefs.get(opponent);

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


    public void finalizeUpdate(Offer o, Player player, Player opponent){
        this.zeroBeliefs.put(opponent, (ZeroBelief) this.update(o, player, opponent));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"agent\": " + agent.getID());
        sb.append(", \"order\" : " + getOrder());
        sb.append(", \"learningSpeed\" : " + getLearningSpeed());
        sb.append(", \"beliefs\" : [");

        for(Map.Entry<Player, ZeroBelief> e : zeroBeliefs.entrySet())
            sb.append("{\"for\" : " + e.getKey().getID() + ", \"vals\": " + e.getValue().toString() + "},");
        sb.deleteCharAt(sb.length()-1);
        sb.append("]}");

        return sb.toString();
    }
    
}
