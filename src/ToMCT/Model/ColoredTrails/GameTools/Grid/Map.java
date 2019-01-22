package ToMCT.Model.ColoredTrails.GameTools.Grid;

import ToMCT.Model.ColoredTrails.Agent.Player;

import ToMCT.Model.Messages.Message;
import ToMCT.Model.Messages.MessageBox;

import java.util.*;

public class Map extends Observable {
    //Holds map of game (all its locations) and current position of players

    private Collection<Player> players; //List of players
    private Hashtable<Player, Location> playersLocation; //Map from player to its location

    private Location[][] locations; //Locations grid
    private Collection<Location> locationList;
    private int width, height; //Dimensions of map

    private MessageBox messageBox; //Message box (to notify players)

    //CONSTRUCTOR
    public Map(int width, int height, MessageBox messageBox, Collection<Player> players){

        this.messageBox = messageBox;

        this.width = width;
        this.height = height;

        this.players = players;
        this.playersLocation = new Hashtable<>();

        this.locationList = new ArrayList<>();
        this.locations = new Location[height][width];

        //Init locations to their respective coordinates (and random trail)
        for(int r=0; r<height; r++) {
            for (int c = 0; c < width; c++) {
                locations[r][c] = new Location(c, r);
                locationList.add(locations[r][c]);

                if(r>0) {
                    locations[r][c].connect(locations[r-1][c]);
                    locations[r-1][c].connect(locations[r][c]);
                }

                if(c>0){
                    locations[r][c].connect(locations[r][c-1]);
                    locations[r][c-1].connect(locations[r][c]);
                }
            }
        }

        resetMap();
    }


    //METHODS

    //Initialize map (players locations)
    public void initialize(Location location){
        movePlayer(location);
        notifyLocations();
    }

    //Initialize map (players locations)
    public void initialize(Hashtable<Player, Location> players){
        movePlayer(players);
        notifyLocations();
    }

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
            playersLocation.get(player).removePlayer(player);

        //Move player to new location (and mark map from player to new location)
        locations[location.getY()][location.getX()].addPlayer(player);
        playersLocation.put(player, locations[location.getY()][location.getX()]);

        //Notify player of it's new position
        Location newLocation = locations[location.getY()][location.getX()];
        Message<Location> locationMessage = new Message<>(this, newLocation);

        messageBox.notifyPlayers(player, locationMessage);
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
    public Collection<Player> getLocationPlayer(Location location){
        return locations[location.getY()][location.getX()].getPlayers();
    }

    //From player to its location
    public Location getPlayerLocation(Player player){
        if(playersLocation.containsKey(player))
            return playersLocation.get(player);

        return null;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Location[][] getLocations(){
        return locations;
    }

    public Collection<Location> getLocationList() {
        return locationList;
    }

    public Location getRandomGoal(){

        Random rand = new Random();

        int offset_x = rand.nextInt(2);
        int side_x = rand.nextInt(2);

        int offset_y = rand.nextInt(2);
        int side_y = rand.nextInt(2);

        int y = (side_y>0) ? (offset_y) : (height-offset_y);
        int x = (side_x>0) ? (offset_x) : (height-offset_x);

        return locations[x][y];

    }
}
