package ToMCT.Model.ColoredTrails.GameTools;

import ToMCT.Model.ColoredTrails.Agent.Player;
import ToMCT.Model.ColoredTrails.GameUtils.Location;

import ToMCT.Model.Messages.Message;
import ToMCT.Model.Messages.MessageBox;

import java.util.Collection;
import java.util.Hashtable;

import java.util.Observable;

public class Map extends Observable {

    private Collection<Player> players;
    private Hashtable<Player, Location> playersLocation;

    private Location[][] locations;
    private int width, height;

    private MessageBox messageBox;

    public Map(MessageBox messageBox, int width, int height, Collection<Player> players){

        this.messageBox = messageBox;

        this.width = width;
        this.height = height;

        this.players = players;
        this.playersLocation = new Hashtable<>();

        this.locations = new Location[height][width];

        for(int r=0; r<height; r++)
            for(int c=0; c<width; c++)
                locations[r][c].set(c,r);

        resetMap();
    }

    public void notifyLocations(){
        messageBox.notifyPlayers(new Message<>(this, playersLocation));
    }

    public void movePlayer(Player player, Location location){

        this.playersLocation.get(player).removePlayer();

        locations[location.getY()][location.getX()].setPlayer(player);
        this.playersLocation.put(player, locations[location.getY()][location.getX()]);
    }

    public void positionPlayers(Hashtable<Player, Location> players){

        resetMap();

        for(Player player : players.keySet())
            movePlayer(player, players.get(player));
    }

    public void positonPlayers(Location location){

        resetMap();

        for(Player player : players)
            movePlayer(player, location);
    }

    public void resetMap(){

        this.playersLocation.clear();

        for(int r=0; r<height; r++)
            for(int c=0; c<width; c++)
                locations[r][c].removePlayer();
    }

    public Player getLocationPlayer(Location location){
        return locations[location.getY()][location.getX()].getPlayer();
    }

    public Location getPlayerLocation(){
        return playersLocation.get(players);
    }
}
