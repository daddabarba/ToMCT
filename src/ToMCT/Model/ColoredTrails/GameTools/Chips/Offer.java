package ToMCT.Model.ColoredTrails.GameTools.Chips;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Offer {

    // FIELDS

    private Player sender, receiver;
    private int got, given;
    private int plate;

    public enum Intention{
        WITHDRAW, ACCEPT, MAKEOFFER;
    }

    private Intention intention;

    public static class OfferIterator implements Iterator<Offer>{

        private Player sender, receiver;
        private Offer currentOffer;
        private int plate;

        private int count;
        private int max;

        public OfferIterator(int plate, Player sender, Player receiver){

            this.sender = sender;
            this.receiver = receiver;

            this.plate = plate;

            count = 0;
            max = 1;

            int tempPlate = plate;
            for(int i=0; i<Chip.values().length; i++) {
                max *= (tempPlate%10 + 1);
                tempPlate = tempPlate/10;
            }

            max -= 1;

            currentOffer = new Offer(sender, receiver, 0);
        }

        @Override
        public boolean hasNext() {
            return max > count;
        }

        @Override
        public Offer next() {

            if(!hasNext())
                return null;

            int tempGiven = currentOffer.getGiven();
            int tempPlate = plate;

            int pos = 1;

            int maxChip, currentChip, newVal, removed = 0, added = 0;

            for(int i=0; i<Chip.values().length; i++){

                maxChip = tempPlate%10;
                currentChip = tempGiven%10;

                newVal = (currentChip+1)%(maxChip+1);

                removed += pos*currentChip;
                added += pos*newVal;

                if(newVal > 0) {
                    count+=1;
                    break;
                }

                tempGiven/=10;
                tempPlate/=10;
                pos *= 10;
            }

            currentOffer.setGiven(currentOffer.getGiven() - removed + added);
            return currentOffer;

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

    public Offer(Player sender, Player receiver, int offered){
        this(sender, receiver, offered, Intention.MAKEOFFER);
    }

    //Given the two exchanging player, and the hand offered to the receiver
    public Offer(Player sender, Player receiver, int offered, Intention intention){

        this.intention = intention;

        this.sender = sender;
        this.receiver = receiver;

        if(sender!=null && receiver!=null) {
            plate = getPlate(sender, receiver);
            this.got = plate - offered;
            this.given = offered;
        }else {
            this.plate = -1;
            this.got = -1;
            this.given = -1;
        }

    }

    // METHODS

    //Turn around offer (change prospective)
    public void invert(){

        if(got<0 || given<0)
            return;

        int temp = this.got;
        Player tempPlayer = this.sender;

        this.got = this.given;
        this.given = temp;

        this.sender = this.receiver;
        this.receiver = tempPlayer;
    }

    public static int getPlate(Player sender, Player receiver){
        return sender.getHand()+ receiver.getHand();
    }

    public static Iterator<Offer> getIterator(Player sender, Player receiver){
        return getIterator(getPlate(sender, receiver), sender, receiver);
    }

    public static Iterator<Offer> getIterator(int plate, Player sender, Player receiver){
        return new OfferIterator(plate, sender, receiver);
    }

    // GETTERS

    public Player getSender(){
        return this.sender;
    }
    public Player getReceiver(){
        return this.receiver;
    }

    public int getGot(){
        return this.got;
    }

    public int getGiven(){
        return this.given;
    }

    public int getPlate(){
        return this.plate;
    }

    public boolean isWithdraw(){
        return intention.equals(Intention.WITHDRAW);
    }

    public boolean isAccept(){
        return intention.equals(Intention.ACCEPT);
    }

    // SETTERS

    public void setGot(int hand){
        this.got = hand;
        this.given = plate - hand;
    }

    public void setGiven(int hand){
        this.given = hand;
        this.got = plate - hand;
    }

    // DATA STORAGE

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("{ \"sender_ID\": \"" + getSender().getID()+"\", ");
        sb.append("\"receiver_ID\": \"" + getReceiver().getID()+"\", ");
        sb.append("\"plate\": \"" + HandUtils.toString(plate)+"\", ");
        sb.append("\"got\": \"" + HandUtils.toString(got)+"\", ");
        sb.append("\"given\": \"" + HandUtils.toString(given)+"\"}");

        return sb.toString();
    }
}
