package ToMCT.Model.ColoredTrails.GameTools;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameUtils.Location;

import ToMCT.Model.Messages.Message;
import ToMCT.Model.Messages.MessageBox;

import java.util.Collection;
import java.util.Hashtable;

import java.util.Observable;

public class Map extends Observable {
    //Holds map of game (all its locations) and current position of players

    private Collection<Player> players; //List of players
    private Hashtable<Player, Location> playersLocation; //Map from player to its location

    private Location[][] locations; //Locations grid
    private int width, height; //Dimensions of map

    private MessageBox messageBox; //Message box (to notify players)

    //CONSTRUCTOR
    public Map(MessageBox messageBox, int width, int height, Collection<Player> players){

        this.messageBox = messageBox;

        this.width = width;
        this.height = height;

        this.players = players;
        this.playersLocation = new Hashtable<>();

        this.locations = new Location[height][width];

        //Init locations to their respective coordinates (and random trail)
        for(int r=0; r<height; r++)
            for(int c=0; c<width; c++)
                locations[r][c] = new Location(c,r);

        resetMap();
    }


    //METHODS

    //Notify player of their locations and the opponents'
    public void notifyLocations(){
        Hashtable<Player, Message<Location>> locationsNotification = new Hashtable<>();

        for(Player player : playersLocation.keySet())
            locationsNotification.put(player, new Message<>(this, playersLocation.get(player)));

        messageBox.notifyPlayers(locationsNotification);
    }

    //Move player to a given location (only x and y of the given location are considered)
    public void movePlayer(Player player, Location location){

        //Remove player from previous location
        if(playersLocation.containsKey(player))
            playersLocation.get(player).removePlayer();

        //Move player to new location (and mark map from player to new location)
        locations[location.getY()][location.getX()].setPlayer(player);
        playersLocation.put(player, locations[location.getY()][location.getX()]);
    }

    //Moves each player to a specified location
    public void movePlayer(Hashtable<Player, Location> players){

        //Remove all players from map
        resetMap();

        //For each player
        for(Player player : players.keySet())
            //Move it to specified location
            movePlayer(player, players.get(player));
    }

    //Moves all players to the same location
    public void movePlayer(Location location){

        //Remove all players from map
        resetMap();

        //For each player
        for(Player player : players)
            //Move it to the given location
            movePlayer(player, location);
    }

    //removes all players from map
    public void resetMap(){

        //Forget previous players location
        this.playersLocation.clear();

        //remove players from each location
        for(int r=0; r<height; r++)
            for(int c=0; c<width; c++)
                locations[r][c].removePlayer();
    }

    //GETTERS

    //From location to the player there
    public Player getLocationPlayer(Location location){
        return locations[location.getY()][location.getX()].getPlayer();
    }

    //From player to its location
    public Location getPlayerLocation(Player player){
        if(playersLocation.containsKey(player))
            return playersLocation.get(player);

        return null;
    }
}
