package ToMCT.Model.ColoredTrails.GameTools;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameUtils.Chip;
import ToMCT.Model.Messages.*;

import java.util.Collection;
import java.util.Hashtable;

import java.util.Observable;

public class Deck extends Observable {

    private Collection<Player> players;
    private Hashtable<Player, Chip[]> hands;

    private MessageBox messageBox;

    public Deck(MessageBox messageBox, Collection<Player> players){
        this.players = players;
        this.messageBox = messageBox;

        this.resetHands();
    }

    public void shuffle(int handSize){
        initHands(handSize);
        notifyHands();
    }

    private void notifyHands(){
        messageBox.notifyPlayers(new Message<>(this, hands));
    }

    private void initHands(int handSize){

        for(Player player : players)
            hands.put(player, generateHand(handSize));

    }

    private Chip[] generateHand(int handSize){
        Chip[] hand = new Chip[handSize];

        for(int i=0; i<handSize; i++)
            hand[i] = Chip.randomChip();

        return hand;
    }

    public void resetHands(){
        this.hands = new Hashtable<>();
    }
}
