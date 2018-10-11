package ToMCT.Model.ColoredTrails.GameUtils;

import java.util.Random;

public enum Chip {

    BLACK(Trail.BLACK),
    WHITE(Trail.WHITE),
    RED(Trail.RED),
    GREEN(Trail.GREEN),
    BLUE(Trail.BLUE);

    private Trail trail;

    private Chip(Trail trail){
        this.trail = trail;
    }

    public Trail trail(){
        return trail;
    }

    public static Chip randomChip(){
        return values()[new Random().nextInt(Trail.nTrails())];
    }
}
