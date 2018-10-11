package ToMCT.Model.ColoredTrails.GameUtils;

import java.awt.Color;
import java.util.Random;

public enum Chip {
    BLACK(0, Color.BLACK),
    WHITE(1, Color.WHITE),
    RED(2, Color.RED),
    GREEN(3, Color.GREEN),
    BLUE(4, Color.BLUE);

    private int val;
    private Color color;

    private Chip(int val, Color color){
        this.val = val;
        this.color = color;
    }

    public int val(){
        return val;
    }

    public Color color(){
        return color;
    }
    
    public static Chip chip(int i){
        return values()[i];
    }

    public static int nChipTypes(){
        return values().length;
    }

    public static Chip randomChip(){
        return values()[new Random().nextInt(nChipTypes())];
    }
}
