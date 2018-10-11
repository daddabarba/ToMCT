package ToMCT.Model.ColoredTrails.GameUtils;

import java.awt.Color;

public enum Trail {
    BLACK(0, Color.BLACK),
    WHITE(1, Color.WHITE),
    RED(2, Color.RED),
    GREEN(3, Color.GREEN),
    BLUE(4, Color.BLUE);

    private int val;
    private Color color;

    private Trail(int val, Color color){
        this.val = val;
        this.color = color;
    }

    public int val(){
        return val;
    }

    public Color color(){
        return color;
    }

    public static Trail trail(int i){
        return values()[i];
    }

    public static int nTrails(){
        return values().length;
    }
}
