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

    public enum Intention{
        WITHDRAW, ACCEPT, MAKEOFFER;
    }

    private Intention intention;

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

    public Offer(Offer o, Intention intention){
        this(o);
        this.intention = intention;
    }

    public Offer(Offer o){
        this(o.getSender(), o.getReceiver(), o.getGiven());
    }

    public Offer(Intention intention){
        this(null, null, null, intention);
    }

    public Offer(Player sender, Player receiver, Hand offered){
        this(sender, receiver, offered, Intention.MAKEOFFER);
    }

    //Given the two exchanging player, and the hand offered to the receiver
    public Offer(Player sender, Player receiver, Hand offered, Intention intention){

        this.intention = intention;

        if(sender!=null && receiver!=null) {
            plate = getPlate(sender, receiver);
            this.got = new AbstractMap.SimpleEntry<>(sender, plate.sub(offered));
            this.given = new AbstractMap.SimpleEntry<>(receiver, offered.copy());
        }else {
            this.plate = null;
            this.got = null;
            this.given = null;
        }

    }

    // METHODS

    //Turn around offer (change prospective)
    public void invert(){

        if(got==null || given==null)
            return;

        Map.Entry<Player, Hand> temp = this.got;

        this.got = this.given;
        this.given = temp;
    }

    public static Hand getPlate(Player sender, Player receiver){
        return sender.getHand().add(receiver.getHand());
    }

    public static Iterator<Offer> getIterator(Player sender, Player receiver){
        return getIterator(getPlate(sender, receiver), sender, receiver);
    }

    public static Iterator<Offer> getIterator(Hand plate, Player sender, Player receiver){
        return new OfferIterator(plate, sender, receiver);
    }

    // GETTERS

    public Player getSender(){
        if(got==null)
            return null;

        return this.got.getKey();
    }
    public Player getReceiver(){
        if(given==null)
            return null;

        return this.given.getKey();
    }

    public Hand getGot(){
        if(got==null)
            return null;

        return this.got.getValue();
    }

    public Hand getGiven(){
        if(given==null)
            return null;

        return this.given.getValue();
    }

    public Hand getPlate(){
        if(plate==null)
            return null;

        return plate;
    }

    public boolean isWithdraw(){
        return intention.equals(Intention.WITHDRAW);
    }

    public boolean isAccept(){
        return intention.equals(Intention.ACCEPT);
    }

    // DATA STORAGE

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("{ \"sender_ID\": \"" + getSender().getID()+"\", ");
        sb.append("\"receiver_ID\": \"" + getReceiver().getID()+"\", ");
        sb.append("\"plate\": \"" + plate.toString()+"\", ");
        sb.append("\"got\": \"" + getGot().toString()+"\", ");
        sb.append("\"given\": \"" + getGiven().toString()+"\"}");

        return sb.toString();
    }
}
