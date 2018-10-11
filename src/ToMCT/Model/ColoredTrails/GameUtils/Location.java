package ToMCT.Model.ColoredTrails.GameUtils;

public class Location {
    private int x,y;

    public Location(){
        this(0,0);
    }

    public Location(int x, int y){
        set(x,y);
    }

    public void set(int x, int y){
        setX(x);
        setY(y);
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.x = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
