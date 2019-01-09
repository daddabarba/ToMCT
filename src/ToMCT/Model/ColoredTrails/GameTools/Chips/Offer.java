package ToMCT.Model.ColoredTrails.GameTools.Chips;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Offer {

    // FIELDS

    private Map.Entry<Player, Hand> got, given;
    private Hand plate;

    public static class OfferIterator implements Iterator<Offer>{

        private Player sender, receiver;
        private Hand currentOffer;
        private Hand plate;

        private int count;
        private int max;

        public OfferIterator(Hand plate, Player sender, Player receiver){

            this.sender = sender;
            this.receiver = receiver;

            this.plate = plate;

            count = 0;
            max = 1;

            for(Integer i : plate.getChipCount().values())
                max *= (i+1);

            max -= 1;

            currentOffer = new Hand(Chip.values().length);
            currentOffer.resetHand();
        }

        @Override
        public boolean hasNext() {
            return max > count;
        }

        @Override
        public Offer next() {

            if(!hasNext())
                return null;

            for(Chip chip : Chip.values()){

                int maxChip = plate.getChipCount(chip);
                int currentChip = currentOffer.getChipCount(chip);

                int newVal = (currentChip+1)%(maxChip+1);

                currentOffer.updateChip(chip, newVal - currentChip);

                if(newVal > 0)
                    return new Offer(sender, receiver, currentOffer);
            }

            return new Offer(sender, receiver, currentOffer);

        }



    }

    // CONSTRUCTOR

    //Given the two exchanging player, and the hand offered to the receiver
    public Offer(Player sender, Player receiver, Hand offered){

        plate = sender.getHand().add(receiver.getHand());

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

    public static Iterator<Offer> getIterator(Hand plate, Player sender, Player receiver){
        return new OfferIterator(plate, sender, receiver);
    }

    // GETTERS

    public Player getSender(){
        return this.got.getKey();
    }
    public Player getReceiver(){
        return this.given.getKey();
    }

    public Hand getGot(){
        return this.got.getValue();
    }

    public Hand getGiven(){
        return this.given.getValue();
    }

    public Hand getPlate(){
        return plate;
    }
}
