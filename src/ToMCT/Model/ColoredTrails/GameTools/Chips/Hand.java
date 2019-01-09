package ToMCT.Model.ColoredTrails.GameTools.Chips;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;
import ToMCT.Model.ColoredTrails.GameUtils.QObservable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

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

    //Add one chip of a given type
    public void updateChip(Chip chip){
        updateChip(chip, Chip.Action.ADD);
    }

    //Adds or remove (action) one chip of a given type
    public void updateChip(Chip chip, Chip.Action action){
        updateChip(new Hashtable<Chip, Integer>(){{put(chip, 1);}} , action);
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

        for(Chip chip : this.chipCount.keySet())
            newCount.put(chip, h.getChipCount().get(chip) + action.val()*this.chipCount.get(chip));

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

}
