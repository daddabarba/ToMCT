package ToMCT.Model.Messages;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

public class MessageBox extends Observable {
    //This class handles messages from the game model to the players.

    //Send the same message to all players
    public void notifyPlayers(Message<?> message){
        setChanged();
        notifyObservers(message);
    }

    //Send a specific message to each player
    public void notifyPlayers(Hashtable<? extends Observer, ? extends Message<?>> messages){

        //For each player
        for(Observer observer : messages.keySet())
            notifyPlayers(observer, messages.get(observer));

    }

    //Notify single player
    public void notifyPlayers(Observer observer, Message message){
        //Deliver its special message
        observer.update(this, message);
    }

}


