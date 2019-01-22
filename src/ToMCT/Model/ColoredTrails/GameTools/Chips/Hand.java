package ToMCT.Model.ColoredTrails.GameTools.Chips;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameUtils.QObservable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class Hand extends QObservable {
    //Object wrapping the hand (chips) of a player

    private Hashtable<Chip, Integer> chipCount; //Number of chips of each type
    private int total;

    //CONSTRUCTOR

    public Hand(){
        chipCount = new Hashtable<>();
        resetHand();
    }

    public Hand(Collection<Chip> chips){
        this();

        updateChip(chips);
    }

    public Hand(int size){
        this(generateHand(size));
    }

    public Hand(Hashtable<Chip, Integer> chipCount){
        this();

        updateChip(chipCount);
    }

    //METHODS

    //Set hand
    public void setHand(Hand hand){

        resetHand();

        for(Map.Entry<Chip, Integer> e : hand.getChipCount().entrySet())
            chipCount.put(e.getKey(), e.getValue());

    }

    //Add one chip of a given type
    public void updateChip(Chip chip){
        updateChip(chip, 1);
    }

    //Adds or remove (action) one chip of a given type
    public void updateChip(Chip chip, Chip.Action action){
        updateChip(chip, 1, action);
    }

    public void updateChip(Chip chip, Integer value){
        updateChip(chip, value, Chip.Action.ADD);
    }

    public void updateChip(Chip chip, Integer value, Chip.Action action){
        updateChip(new Hashtable<Chip, Integer>(){{put(chip, value);}} , action);
    }

    //Adds several chips (in a collection)
    public void updateChip(Collection<Chip> chips){
        updateChip(chips, Chip.Action.ADD);
    }

    //Adds or remove (action) several chips (in a collection)
    public void updateChip(Collection<Chip> chips, Chip.Action action){

        for(Chip chip : chips)
            updateChip(chip, action);
    }

    //Increases the count fo each type of chip by a certain delta
    public void updateChip(Hashtable<Chip, Integer> deltas){
        updateChip(deltas, Chip.Action.ADD);
    }

    //Increases or decreases (action) the count fo each type of chip by a certain delta
    public void updateChip(Hashtable<Chip, Integer> deltas, Chip.Action action){

        for(Chip chip : deltas.keySet()) {
            chipCount.put(chip, chipCount.get(chip) + action.val() * deltas.get(chip));
            total += action.val() * deltas.get(chip);
        }

        quickNotification();
    }

    //Compare two hands
    @Override
    public boolean equals(Object o){

        if(o == this)
            return true;

        if(!(o instanceof Hand))
            return false;

        Hand h1 = (Hand)o;

        boolean ret = true;

        if(total != h1.getTotal())
            return false;

        for(Chip chip : chipCount.keySet())
            ret = ret && (chipCount.get(chip).equals(h1.getChipCount().get(chip)));

        return ret;
    }

    public boolean isempty(){
        return total < 1;
    }

    //Make a copy of the hand
    public Hand copy(){
        return new Hand(this.chipCount);
    }

    //Sum two hands
    public Hand add(Hand h){
        return this.mix(h, Chip.Action.ADD);
    }

    //Subtract two hands
    public Hand sub(Hand h){
        return this.mix(h, Chip.Action.REMOVE);
    }

    private Hand mix(Hand h, Chip.Action action){
        if(h.getChipCount().size() != this.chipCount.size())
            return null;

        Hashtable<Chip, Integer> newCount = new Hashtable<>();

        for(Map.Entry<Chip, Integer> e : this.chipCount.entrySet())
            newCount.put(e.getKey(), e.getValue() + action.val() * h.getChipCount().get(e.getKey()));

        return new Hand(newCount);
    }

    //reset chip counts to all zeros
    public void resetHand(){

        for(Chip chip : Chip.values())
            chipCount.put(chip, new Integer(0));

        total = 0;
    }

    //GETTERS

    public Hashtable<Chip, Integer> getChipCount(){
        return chipCount;
    }

    public int getChipCount(Chip chip){
        return chipCount.get(chip);
    }

    public int getTotal(){
        return total;
    }

    //STATIC METHODS

    //Combines two hands into one
    public static Hand combine(Hand h1, Hand h2){

        Hashtable<Chip, Integer> sum = new Hashtable<>();

        for(Chip chip : h1.getChipCount().keySet())
            sum.put(chip, h1.getChipCount().get(chip) + h2.getChipCount().get(chip));

        return new Hand(sum);
    }

    //Compute delta (difference) between two hads
    public static Hashtable<Chip, Integer> delta(Hand h1, Hand h2){

        Hashtable<Chip, Integer> delta = new Hashtable<>();

        for(Chip chip : h1.getChipCount().keySet())
            delta.put(chip, h1.getChipCount().get(chip) - h2.getChipCount().get(chip));

        return delta;
    }

    //Initialize random hands of given size
    public static Collection<Chip> generateHand(int handSize){
        Collection<Chip> hand = new ArrayList<>();

        for(int i=0; i<handSize; i++)
            hand.add(Chip.randomChip());

        return hand;
    }

    // DATA STORAGE


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ");
        for(Map.Entry<Chip, Integer> e : chipCount.entrySet())
            sb.append("\"" + e.getKey().toString() + "\":\"" + e.getValue() + "\",");

        String ret = sb.toString();
        ret = ret.substring(0, ret.length()-1) + "}";

        return ret;
    }
}
