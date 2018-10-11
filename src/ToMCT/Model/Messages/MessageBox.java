package ToMCT.Model.Messages;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

public class MessageBox extends Observable {

    public void notifyPlayers(Message<?> message){
        setChanged();
        notifyObservers(message);
    }

    public void notifyPlayers(Hashtable<? extends Observer, Message<?>> messages){

        for(Observer observer : messages.keySet())
            observer.update(this, messages.get(observer));

    }

}


