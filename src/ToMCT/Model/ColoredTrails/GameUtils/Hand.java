package ToMCT.Model.ColoredTrails.GameUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class Hand {
    //Object wrapping the hand (chips) of a player

    private Hashtable<Chip, Integer> chipCount; //Number of chips of each type


    //CONSTRUCTOR

    public Hand(int size){
        this(generateHand(size));
    }

    public Hand(Collection<Chip> chips){
        chipCount = new Hashtable<>();
        resetHand();

        updateChip(chips);
    }

    public Hand(Hashtable<Chip, Integer> chipCount){
        chipCount = new Hashtable<>();
        resetHand();

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

        for(Chip chip : deltas.keySet())
          chipCount.put(chip, chipCount.get(chip) + action.val()*deltas.get(chip));
    }

    //Compare two hands
    public boolean equals(Hand h1){
        boolean ret = true;

        for(Chip chip : chipCount.keySet())
            ret = ret && (chipCount.get(chip).equals(h1.getChipCount().get(chip)));

        return ret;
    }

    //reset chip counts to all zeros
    public void resetHand(){

        for(Chip chip : chipCount.keySet())
            chipCount.put(chip, new Integer(0));
    }

    //GETTERS

    public Hashtable<Chip, Integer> getChipCount(){
        return chipCount;
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
