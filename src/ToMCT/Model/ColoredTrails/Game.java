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
import ToMCT.Model.ColoredTrails.GameUtils.TimeKeeper;
import ToMCT.Model.Messages.MessageBox;

import java.util.*;

public class Game extends Observable implements ScoreKeeper, Mediator, TimeKeeper {
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

    private class SingleGameData{

        private HashMap<Integer, Double> finalScores;
        private String finalPlayers;
        private MatchTimeKeeper matchTimeKeeper;

        public SingleGameData(){
            this(null);
        }

        public SingleGameData(MatchTimeKeeper matchTimeKeeper){
            this.finalScores = new HashMap<>();
            this.matchTimeKeeper = matchTimeKeeper;

            writeFinalScores();
            writeFinalPlayers();
        }

        public SingleGameData writeFinalPlayers(){
            StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            for(Player player : Game.this.getPlayers())
                sb.append("\"" + player.getID() + "\" : " + player.toString() + ",");
            sb.deleteCharAt(sb.length()-1);
            sb.append("}");
            this.finalPlayers = sb.toString();
            return this;
        }

        public SingleGameData writeFinalScores(){
            for(Player player : Game.this.getPlayers())
                finalScores.put(player.getID(), score(player.getHand(), player.getPosition(), Game.this.goals.get(player)));
            return this;
        }

        public String toString(){
            String ret =  "{\"final_scores\": " + getFinalScoreData() + ", \"finalPlayers\" : " + this.finalPlayers;

            if(this.matchTimeKeeper!=null)
                ret += ", " + matchTimeKeeper.toString();

            return ret + "}";
        }

        // DATA COLLECTION
        private String getFinalScoreData(){
            StringBuilder sb = new StringBuilder();
            sb.append("{ ");

            for(java.util.Map.Entry<Integer, Double> e : finalScores.entrySet())
                sb.append("\"" + e.getKey() + "\":\"" + e.getValue() + "\",");

            sb.deleteCharAt(sb.length()-1);
            sb.append("}");

            return sb.toString();
        }

    }

    private class MatchTimeKeeper{

        private Player player1, player2;
        private StringBuilder steps;

        public MatchTimeKeeper(Player player1, Player player2){
            this.player1 = player1;
            this.player2 = player2;

            steps = new StringBuilder();
            steps.append("\"steps\" : [ ");
        }

        public void recordTime(Offer o){
            steps.append("{\"players\" : {");
            steps.append("\" " + player1.getID() +" \" : " + player1.toString());
            steps.append(", \" " + player2.getID() +" \" : " + player2.toString() + "}");
            steps.append(", \"offer\" : " + o.toString() + "},");
        }

        public String toString(){
            steps.deleteCharAt(steps.length()-1);
            steps.append("]");
            String ret = steps.toString();
            steps.deleteCharAt(steps.length()-1);
            steps.append(",");
            return ret;
        }
    }

    private Collection<Player> players; //List of participants
    private HashMap<Player, Location> goals;
    private Collection<SingleGameData> gameData;
    private Player startingPlayer;

    private Map map; //Game map
    private Deck deck; //Game deck

    private MessageBox messageBox;

    private int handSize;
    private Location start;

    private Collection<String> offersMade;

    private HashMap<AgentState, Double> scoreTable;

    private MatchTimeKeeper steps;

    public Game(Integer[] orders, Double[] learningSpeeds, int handSize, int mazeSize){
        this(orders, learningSpeeds, handSize, mazeSize, new Location((int)mazeSize/2, (int)mazeSize/2));
    }

    public Game(Integer[] orders, Double[] learningSpeeds, int handSize, int mazeSize, Location start){

        this.handSize = handSize;
        this.start = start;

        messageBox = new MessageBox();

        players = new ArrayList<>();
        gameData = new ArrayList<>();

        for(int i=0; i<orders.length; i++) {
            Player player = new Player(i,this, this, this);

            if(i==0)
                startingPlayer = player;

            players.add(player);
            messageBox.addObserver(player);
        }

        map = new Map(mazeSize, mazeSize, messageBox, players, start);
        deck = new Deck(messageBox, players);

        scoreTable = new HashMap<>();

        int i=0;
        for(Player player :players) {
            player.init(orders[i], learningSpeeds[i], players, getMap());
            i++;
        }
    }

    public void startGame(){
        offersMade = new ArrayList<>();
        goals = new HashMap<>();

        deck.shuffle(handSize);
        map.initialize(start);
        
        for(Player player :players) {
            Location goal = map.getRandomGoal();
            player.setGoal(goal);
            goals.put(player, goal);
        }
    }

    public void play(){
        this.play(1);
    }

    public void play(int times){

        for(Player player : players){
            if(!player.equals(startingPlayer)){
                this.play(startingPlayer, player, times);
                return;
            }
        }
    }

    public void play(Player player1, Player player2, int times){

        for(int i=0; i<times; i++)
            this.play(player1, player2);
    }

    public void play(Player player1, Player player2){

        this.startGame();

        steps = new MatchTimeKeeper(player1, player2);

        startingPlayer.Play();
        gameData.add(new SingleGameData(steps));
    }

    public void timeHasPassed(Player player1, Player player2, Offer o){
        steps.recordTime(o);
    }

    // METHODS

    // Mediator
    @Override
    public void mediate(Offer offer){
        Player sender = offer.getSender();
        Player receiver = offer.getReceiver();

        if(Offer.getPlate(sender, receiver)==(offer.getPlate())) {
            steps.recordTime(offer);

            if (!offer.isWithdraw()) {
                String offerMade = offer.toString();
                offersMade.add(offerMade);

                sender.setHand(offer.getGot());
                receiver.setHand(offer.getGiven());

            }
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

    // DATA
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"map\" : " + map.toString());
        sb.append(", \"games\" : [ ");
        for(SingleGameData gd : gameData)
            sb.append(gd.toString() + ",");
        sb.deleteCharAt(sb.length()-1);
        sb.append("]}");

        return sb.toString();
    }
}
