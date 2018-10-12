package ToMCT.Model.ColoredTrails.GameUtils;

import java.awt.Color;
import java.util.Random;

public enum Trail {
    //Placeholder for trail (either the map location's or the chip's)

    //VALUES

    BLACK(0, Color.BLACK),
    WHITE(1, Color.WHITE),
    RED(2, Color.RED),
    GREEN(3, Color.GREEN),
    BLUE(4, Color.BLUE);


    private int val; //integer converted trail color
    private Color color; //Actual RGB color

    //CONSTRUCTOR
    private Trail(int val, Color color){
        this.val = val;
        this.color = color;
    }

    //GETTERS
    public int val(){
        return val;
    }

    public Color color(){
        return color;
    }

    //STATIC METHODS

    //Converts and int to its respective Trail
    public static Trail trail(int i){
        return values()[i];
    }

    //Returns the number of different Trails available
    public static int nTrails(){
        return values().length;
    }

    //Returns a random Trail
    public static Trail randomTrail(){
        return values()[new Random().nextInt(nTrails())];
    }
}
