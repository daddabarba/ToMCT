package ToMCT.Model.ColoredTrails.GameUtils;

import java.util.Observable;

public class QObservable extends Observable {

    //Send notification to observer
    public void quickNotification(){
        setChanged();
        notifyObservers();
    }
}
