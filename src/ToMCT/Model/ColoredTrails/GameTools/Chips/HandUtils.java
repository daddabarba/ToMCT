package ToMCT.Model.ColoredTrails.GameTools.Chips;

import ToMCT.Model.ColoredTrails.GameTools.Basic.Chip;

public class HandUtils {
    //Object wrapping the hand (chips) of a player

    //METHODS

    //Adds or remove (action) one chip of a given type
    public static int updateChip(int hand, Chip chip, int action){
        return hand + (int)Math.pow(10, chip.trail().val())*action;
    }

    public static boolean isempty(int hand){
        return hand < 1;
    }

    //GETTERS

    public static int getChipCount(int hand, Chip chip){
        return getChipCount(hand, chip.trail().val());
    }

    public static int getChipCount(int hand, int i){
        return (hand/((int)Math.pow(10, i)))%10;
    }

    public static int getTotal(int hand){
        int sum = 0;

        for(int i=0; i<Chip.values().length; i++) {
            sum += hand%10;
            hand /= 10;
        }

        return sum;
    }

    //Initialize random hands of given size
    public static int randomHand(int handSize){
        int hand = 0;

        for(int i=0; i<handSize; i++)
            hand = updateChip(hand, Chip.randomChip(), 1);

        return hand;
    }

    // DATA STORAGE

    public static String toString(int hand) {
        StringBuilder sb = new StringBuilder();

        sb.append("{ ");
        for(Chip chip : Chip.values())
            sb.append("\"" + chip.toString() + "\":\"" + getChipCount(hand, chip) + "\",");

        String ret = sb.toString();
        ret = ret.substring(0, ret.length()-1) + "}";

        return ret;
    }
}
