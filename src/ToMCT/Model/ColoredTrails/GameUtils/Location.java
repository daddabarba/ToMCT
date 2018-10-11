package ToMCT.Model.ColoredTrails.GameUtils;

import ToMCT.Model.ColoredTrails.Agent.Player;

public class Location {
    private int x,y;
    private Player player;

    private Trail trail;

    public Location(){
        this(0,0);
    }

    public Location(int x, int y){
        this(x, y, Trail.randomTrail());
    }

    public Location(int x, int y, Trail trail){
        set(x,y);
        setPlayer(null);

        this.trail = trail;
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

    public void removePlayer(){
        setPlayer(null);
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Player getPlayer(){
        return player;
    }
}
