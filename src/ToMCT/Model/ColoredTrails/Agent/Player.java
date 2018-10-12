package ToMCT.Model.ColoredTrails.Agent;

import ToMCT.Model.ColoredTrails.GameUtils.Hand;
import ToMCT.Model.ColoredTrails.GameUtils.Location;

import ToMCT.Model.ColoredTrails.GameTools.*;

import ToMCT.Model.Messages.Message;
import ToMCT.Model.Messages.MessageBox;

import java.util.Observable;
import java.util.Observer;

public class Player implements Observer {
    //Player class (interface between ToM agent and game)

    private Hand hand; //Player's hand

    private Location goal, position; // The player's goal and current position

    public Player(){
        hand = new Hand();
    }

    //SETTER

    private void setPosition(Location position){
        this.position = position;
    }

    private void setHand(Hand hand){
        this.hand = hand;
    }

    //GETTER

    public Location getPosition(){
        return position;
    }

    public Hand getHand(){
        return hand;
    }

    //NOTIFICATION HANDLER

    @Override
    public void update(Observable observable, Object message){

        if( observable instanceof MessageBox && message instanceof Message )
            handleUpdate((MessageBox) observable, (Message)message);
        
    }

    private void handleUpdate(MessageBox messageBox, Message message){

        if( message.getSender().getClass().equals(Map.class) )
            updateFromMap(message);
        else if( message.getSender().getClass().equals(Deck.class) )
            updateFromDeck(message);

    }

    private void updateFromMap(Message message){

        if(message.getContent() instanceof Location)
            if(message.getContext() == null)
                setPosition(((Message<Location>)message).getContent());
    }

    private void updateFromDeck(Message message){

        if(message.getContent() instanceof Hand)
            if(message.getContext() == null)
                setHand(((Message<Hand>)message).getContent());
    }


}
