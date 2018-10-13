package ToMCT.Model.ColoredTrails.GameUtils;

import ToMCT.Model.ColoredTrails.Agent.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

public class Location extends Observable {
    //Class to hold information regarding a location on the map

    private int x, y; //Its x(column) and y(row) coordinates respectively
    private Collection<Player> players; //The player in that location (null if there is no player)

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
        removePlayer();

        this.trail = trail;
    }

    //Send notification to observer
    private void quickNotification(){
        setChanged();
        notifyObservers();
    }

    //SETTERS

    public void set(int x, int y){
        setX(x);
        setY(y);
    }

    public void setX(int x){
        this.x = x;

        quickNotification();
    }

    public void setY(int y){
        this.y = y;

        quickNotification();
    }

    public void removePlayer(){
        players = new ArrayList<>();

        quickNotification();
    }

    public void removePlayer(Player player){
        players.remove(player);

        quickNotification();
    }

    public void addPlayer(Player player){
        if(!players.contains(player))
            players.add(player);

        quickNotification();
    }

    //GETTERS

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Collection<Player> getPlayers(){
        return players;
    }

    public Trail getTrail(){
        return trail;
    }
}
