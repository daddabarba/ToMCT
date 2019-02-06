package ToMCT.Model.ColoredTrails;

import ToMCT.Model.ColoredTrails.Agent.Player;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Deck;
import ToMCT.Model.ColoredTrails.GameTools.Chips.HandUtils;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Map;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;
import ToMCT.Model.ColoredTrails.GameUtils.Mediator;
import ToMCT.Model.ColoredTrails.GameUtils.ScoreKeeper;
import ToMCT.Model.Messages.MessageBox;

import java.util.*;

public class Game extends Observable implements ScoreKeeper, Mediator {
    //Class coordinating the different components of the CT game

    private class AgentState {

        private final int hand;
        private final Location start;
        private final Location goal;

        public AgentState(int hand, Location start, Location goal){
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
            return as.getGoal().equals(goal) && as.getStart().equals(start) && as.getHand()==(hand);
        }

        @Override public int hashCode() {
            return Objects.hash(hand, start, goal);
        }

        public int getHand(){
            return hand;
        }

        public Location getStart(){
            return start;
        }

        public Location getGoal(){
            return goal;
        }
    }
    
    private Collection<Player> players; //List of participants
    private HashMap<Player, Location> goals;
    private Collection<HashMap<Integer, Double>> gameData;
    private Player startingPlayer;

    private Map map; //Game map
    private Deck deck; //Game deck

    private MessageBox messageBox;

    private int handSize;
    private Location start;

    private Collection<String> offersMade;

    private HashMap<AgentState, Double> scoreTable;

    public Game(Integer[] orders, int handSize, int mazeSize){
        this(orders, handSize, mazeSize, new Location((int)mazeSize/2, (int)mazeSize/2));
    }

    public Game(Integer[] orders, int handSize, int mazeSize, Location start){

        this.handSize = handSize;
        this.start = start;

        messageBox = new MessageBox();

        players = new ArrayList<>();
        gameData = new ArrayList<>();

        for(int i=0; i<orders.length; i++) {
            Player player = new Player(i,this, this);

            if(i==0)
                startingPlayer = player;

            players.add(player);
            messageBox.addObserver(player);
        }

        map = new Map(mazeSize, mazeSize, messageBox, players, start);
        deck = new Deck(messageBox, players);

        goals = new HashMap<>();
        scoreTable = new HashMap<>();

        int i=0;
        for(Player player :players) {
            Location goal = map.getRandomGoal();
            player.init(goal, orders[i], players, getMap());
            goals.put(player, goal);

            i++;
        }
    }

    public void startGame(){
        offersMade = new ArrayList<>();

        deck.shuffle(handSize);
        map.initialize(start);
    }

    public void play(){
        startingPlayer.Play();

        HashMap<Integer, Double> matchResult = new HashMap<>();
        for(Player player : players)
            matchResult.put(player.getID(), score(player.getHand(), player.getPosition(), goals.get(player)));
        gameData.add(matchResult);

        System.out.println("\nFinal score: \n" + getFinalScoreData());
    }

    // METHODS

    // Mediator
    @Override
    public void mediate(Offer offer){
        Player sender = offer.getSender();
        Player receiver = offer.getReceiver();

        if(Offer.getPlate(sender, receiver)==(offer.getPlate())) {

            String offerMade = offer.toString();
            offersMade.add(offerMade);
            System.out.println("Offer Made:\n" + offerMade + "\n");

            sender.setHand(offer.getGot());
            receiver.setHand(offer.getGiven());
        }
    }

    // Score Keeper

    @Override
    public double score(int hand, Location start, Location goal){

        AgentState state = new AgentState(hand, start, goal);

        if(scoreTable.containsKey(state))
            return scoreTable.get(state);

        double score = score(hand, start, goal, 0);

        scoreTable.put(state, score);
        return score;
    }

    private double score(int hand, Location current, Location goal, double score){

        if(HandUtils.isempty(hand))
            return score;

        if(current.equals(goal))
            return score + 500 + 50*HandUtils.getTotal(hand);

        Double max = null;

        for(Location l : current.getNeighbours()) {

            Chip chip = Chip.fromTrail(l.getTrail());
            if( HandUtils.getChipCount(hand, chip) > 0 ) {
                int increment = (l.distance(goal)<current.distance(goal)) ? (100) : (0);
                
                double newScore = score(HandUtils.updateChip(hand, chip, -1), l, goal, score+increment);
                if (max == null || newScore > max)
                    max = newScore;
            }
        }

        if(max == null)
            return score + 50*HandUtils.getTotal(hand);

        return max;

    }

    // GETTERS

    public Collection<Player> getPlayers(){
        return players;
    }

    public Map getMap(){
        return map;
    }

    public Deck getDeck(){
        return deck;
    }

    // DATA COLLECTION
    public String getFinalScoreData(){
        StringBuilder sb = new StringBuilder();
        sb.append("{[");

        for(HashMap<Integer, Double> matchResult : gameData) {
            sb.append("{ ");
            for(java.util.Map.Entry<Integer, Double> e : matchResult.entrySet()){
                sb.append("\"" + e.getKey() + "\":\"" + e.getValue() + "\",");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("}");
        }

        sb.append("]}");
        return sb.toString();
    }
}
