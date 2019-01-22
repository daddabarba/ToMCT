package ToMCT.Model.ColoredTrails;

import ToMCT.Model.ColoredTrails.Agent.Player;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Deck;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Hand;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Map;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.Model.ColoredTrails.GameUtils.ScoreKeeper;
import ToMCT.Model.Messages.MessageBox;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

import java.util.*;

public class Game extends Observable implements ScoreKeeper {
    //Class coordinating the different components of the CT game

    private Collection<Player> players; //List of participants

    private Map map; //Game map
    private Deck deck; //Game deck

    private MessageBox messageBox;

    private int handSize;
    private Location start;

    private class AgentState {

        private final Hand hand;
        private final Location start;
        private final Location goal;

        public AgentState(Hand hand, Location start, Location goal){
            this.hand = hand;
            this.start = start;
            this.goal = goal;
        }

        @Override
        public boolean equals(Object o){
            if(o == this)
                return true;

            if(!(o instanceof AgentState))
                return false;

            AgentState as = (AgentState)o;
            return as.getGoal().equals(goal) && as.getStart().equals(start) && as.getHand().equals(hand);
        }

        @Override public int hashCode() {
            return Objects.hash(hand, start, goal);
        }

        public Hand getHand(){
            return hand;
        }

        public Location getStart(){
            return start;
        }

        public Location getGoal(){
            return goal;
        }
    }

    private HashMap<AgentState, Double> scoreTable;

    public Game(int numPlayers, int handSize, int width, int height, Location start){

        this.handSize = handSize;
        this.start = start;

        messageBox = new MessageBox();

        players = new ArrayList<>();

        for(int i=0; i<numPlayers; i++) {
            Player player = new Player(i, (ScoreKeeper)this);

            players.add(player);
            messageBox.addObserver(player);
        }

        map = new Map(width, height, messageBox, players);
        deck = new Deck(messageBox, players);

        scoreTable = new HashMap<>();
    }

    public void startGame(){

        deck.shuffle(handSize);
        map.initialize(start);
    }

    // METHODS

    @Override
    public double score(Hand hand, Location start, Location goal){

        AgentState state = new AgentState(hand, start, goal);

        if(scoreTable.containsKey(state))
            return scoreTable.get(state);

        double score = score(hand, start, goal, 0);

        scoreTable.put(state, score);
        return score;
    }

    private double score(Hand hand, Location current, Location goal, double score){

        if(hand.isempty())
            return score;

        if(current.equals(goal))
            return score + 500 + 50*hand.getTotal();

        Double max = null;

        for(Location l : current.getNeighbours()) {

            Chip chip = Chip.fromTrail(l.getTrail());
            if( hand.getChipCount(chip) > 0 ) {

                hand.updateChip(chip, Chip.Action.REMOVE);

                int increment = (l.distance(goal)<current.distance(goal)) ? (100) : (0);
                
                double newScore = score(hand, l, goal, score+increment);
                if (max == null || newScore > max)
                    max = newScore;

                hand.updateChip(chip, Chip.Action.ADD);
            }
        }

        if(max == null)
            return score + 50*hand.getTotal();

        return max;

    }

    //GETTERS

    public Collection<Player> getPlayers(){
        return players;
    }

    public Map getMap(){
        return map;
    }

    public Deck getDeck(){
        return deck;
    }
}
