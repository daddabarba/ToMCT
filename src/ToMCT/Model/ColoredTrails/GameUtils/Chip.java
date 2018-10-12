package ToMCT.Model.ColoredTrails.GameUtils;

import java.util.Random;

public enum Chip {
    //Placeholder for chip

    //VALUES

    BLACK(Trail.BLACK),
    WHITE(Trail.WHITE),
    RED(Trail.RED),
    GREEN(Trail.GREEN),
    BLUE(Trail.BLUE);

    //Placeholder for possible actions with chip
    public enum Action{
        ADD(1), //receive chip
        REMOVE(-1); //give chip

        private int val; //integer converted action

        //CONSTRUCTOR
        private Action(int val){
            this.val = val;
        }

        //GETTER
        public int val(){
            return val;
        }
    }

    private Trail trail; //Every chip has a trail (color)

    //CONSTRUCTOR
    private Chip(Trail trail){
        this.trail = trail;
    }


    //GETTER
    public Trail trail(){
        return trail;
    }

    //STATIC METHODS

    //Generate random chip
    public static Chip randomChip(){
        return values()[new Random().nextInt(Trail.nTrails())];
    }
}
