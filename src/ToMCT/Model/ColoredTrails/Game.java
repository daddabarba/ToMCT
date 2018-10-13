package ToMCT.Model.ColoredTrails;

import ToMCT.Model.ColoredTrails.Agent.Player;

import ToMCT.Model.ColoredTrails.GameTools.Deck;
import ToMCT.Model.ColoredTrails.GameTools.Map;
import ToMCT.Model.ColoredTrails.GameUtils.Location;
import ToMCT.Model.Messages.MessageBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

public class Game extends Observable {
    //Class coordinating the different components of the CT game

    private Collection<Player> players; //List of participants

    private Map map; //Game map
    private Deck deck; //Game deck

    private MessageBox messageBox;

    private int handSize;
    private Location start;

    public Game(int numPlayers, int handSize, int width, int height, Location start){

        this.handSize = handSize;
        this.start = start;

        messageBox = new MessageBox();

        players = new ArrayList<>();

        for(int i=0; i<numPlayers; i++) {
            Player player = new Player(i);

            players.add(player);
            messageBox.addObserver(player);
        }

        map = new Map(width, height, messageBox, players);
        deck = new Deck(messageBox, players);
    }

    public void startGame(){

        deck.shuffle(handSize);
        map.initialize(start);
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
