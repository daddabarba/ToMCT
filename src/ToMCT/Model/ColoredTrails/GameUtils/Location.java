package ToMCT.Model.ColoredTrails.GameUtils;

import ToMCT.Model.ColoredTrails.Agent.Player;

public class Location {
    //Class to hold information regarding a location on the map

    private int x, y; //Its x(column) and y(row) coordinates respectively
    private Player player; //The player in that location (null if there is no player)

    private Trail trail; //Trail (color) of location

    //CONSTRUCTORS

    //Initialize to (0,0) position
    public Location(){
        this(0,0);
    }

    //Initialize to specified position and random Color
    public Location(int x, int y){
        this(x, y, Trail.randomTrail());
    }

    //Initialize to specific position and assigned color
    public Location(int x, int y, Trail trail){
        set(x,y);
        setPlayer(null);

        this.trail = trail;
    }

    //SETTERS

    public void set(int x, int y){
        setX(x);
        setY(y);
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void removePlayer(){
        setPlayer(null);
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    //GETTERS

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Player getPlayer(){
        return player;
    }

    public Trail getTrail(){
        return trail;
    }
}
