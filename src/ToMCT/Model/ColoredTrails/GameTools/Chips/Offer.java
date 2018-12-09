package ToMCT.Model.ColoredTrails.GameTools.Chips;

import ToMCT.Model.ColoredTrails.Agent.Player;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Offer {

    // FIELDS

    private Map.Entry<Player, Hand> got, given;

    // CONSTRUCTOR

    //Given the two exchanging player, and the hand offered to the receiver
    public Offer(Player sender, Player receiver, Hand offered){

        Hand plate = sender.getHand().add(receiver.getHand());

        this.got = new AbstractMap.SimpleEntry<>(sender, plate.sub(offered));
        this.given = new AbstractMap.SimpleEntry<>(receiver, offered.copy());
    }

    // METHODS

    //Turn around offer (change prospective)
    public void invert(){

        Map.Entry<Player, Hand> temp = this.got;

        this.got = this.given;
        this.given = temp;
    }

    // GETTERS

    public Player getSender(){
        return this.got.getKey();
    }

    public Player getReceiver(){
        return this.given.getKey();
    }
}
