package ToMCT.Model.ColoredTrails.GameTools;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameUtils.*;
import ToMCT.Model.Messages.*;

import java.util.Collection;
import java.util.Hashtable;

import java.util.Observable;

public class Deck extends Observable {
    //Class keeping track of the (initial) hands of all players

    private Collection<Player> players; //List of players
    private Hashtable<Player, Hand> hands; //Map from player to hand

    private MessageBox messageBox; //message box to notify players of their hand
    //CONSTRUCTOR
    public Deck(MessageBox messageBox, Collection<Player> players){
        this.players = players;
        this.messageBox = messageBox;

        this.resetHands();
    }


    //METHODS

    //shuffles deck (generates new hands for each player)
    public void shuffle(int handSize){
        initHands(handSize);
        notifyHands();
    }

    //Notifies players of the hands that each player has
    private void notifyHands(){
        messageBox.notifyPlayers(new Message<>(this, hands));
    }

    //Initialize random hands of given size, for each player
    private void initHands(int handSize){

        for(Player player : players)
            hands.put(player, new Hand(handSize));

    }

    //Resets deck
    public void resetHands(){
        this.hands = new Hashtable<>();
    }
}
