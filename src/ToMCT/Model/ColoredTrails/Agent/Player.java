package ToMCT.Model.ColoredTrails.Agent;

import ToMCT.Model.ColoredTrails.Agent.ToM.HigherToMAgent;
import ToMCT.Model.ColoredTrails.Agent.ToM.ToMAgent;
import ToMCT.Model.ColoredTrails.Agent.ToM.ZeroToMAgent;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Deck;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Hand;
import ToMCT.Model.ColoredTrails.GameTools.Chips.Offer;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Map;
import ToMCT.Model.ColoredTrails.GameTools.Grid.Location;

import ToMCT.Model.ColoredTrails.GameUtils.Mediator;
import ToMCT.Model.ColoredTrails.GameUtils.QObservable;
import ToMCT.Model.ColoredTrails.GameUtils.ScoreKeeper;
import ToMCT.Model.Messages.Message;
import ToMCT.Model.Messages.MessageBox;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

public class Player extends QObservable implements Observer {
    //Player class (interface between ToM agent and game)

    private int ID;

    private Hand hand; //Player's hand

    private Location goal, position; // The player's goal and current position

    private ScoreKeeper scoreKeeper;
    private Mediator mediator;

    private ToMAgent toMAgent;

    private Collection<Player> players;

    //CONSTRUCTOR
    public Player(int ID, ScoreKeeper scoreKeeper, Mediator mediator){
        this.ID = ID;
        hand = new Hand();

        this.scoreKeeper = scoreKeeper;
        this.mediator = mediator;

        this.toMAgent = null;
    }

    public void init(Location goal, int order, Collection<Player> players, Collection<Location> locations){

        this.goal = goal;
        this.players = players;

        if(this.toMAgent==null)
            if(order>0)
                this.toMAgent = new HigherToMAgent(this, order, players, locations);
            else
                this.toMAgent = new ZeroToMAgent(this, players);
    }

    // PLAY

    public void Play(){
        Play(selectOpponent());
    }

    private Player selectOpponent(){
        for(Player player : players)
            if(!player.equals(this))
                return player;

        return null;
    }

    private void Play(Player opponent){

        if(opponent==null)
            return;

        Offer firstOffer = this.toMAgent.bestOffer(this, opponent, goal);
        opponent.makeOffer(this, firstOffer);
    }

    protected void makeOffer(Player opponent,  Offer o){

        if(o.isWithdraw())
            return;

        if(o.isAccept())
            mediator.mediate(o);

        Offer response = this.toMAgent.ToM(o, this, opponent, goal);

        if(response.isWithdraw())
            return;

        if(response.isAccept())
            mediator.mediate(response);

        opponent.makeOffer(this, response);

    }

    // METHODS

    @Override
    public boolean equals(Object object){

        if(object == this)
            return true;

        if(!(object instanceof Player))
            return false;

        Player player = (Player)object;
        return ID == player.getID();
    }

    //SETTER

    private void setPosition(Location position){
        this.position = position;

        quickNotification();
    }

    private void setHand(Hand hand){
        this.hand = hand;

        quickNotification();
    }

    //GETTERS

    public Location getPosition(){
        return position;
    }

    public Hand getHand(){
        return hand;
    }

    public int getID(){
        return ID;
    }

    public ScoreKeeper getScoreKeeper(){
        return scoreKeeper;
    }

    //NOTIFICATION HANDLER

    @Override

    //Handles generic notification
    public void update(Observable observable, Object message){

        if( observable instanceof MessageBox && message instanceof Message )
            handleUpdate((MessageBox) observable, (Message)message);
        
    }

    //Handles notifications from message box
    private void handleUpdate(MessageBox messageBox, Message message){

        //Classifies between notifications from Map
        if( message.getSender().getClass().equals(Map.class) )
            updateFromMap(message);
        //Or from Deck
        else if( message.getSender().getClass().equals(Deck.class) )
            updateFromDeck(message);

    }

    //Handles notification from Map
    private void updateFromMap(Message message){

        //Identifies goal of the message

        //If a location is sent with no context
        if(message.getContent() instanceof Location)
            if(message.getContext() == null)
                //It must be the player's own location
                setPosition(((Message<Location>)message).getContent());
    }

    //Handles notifications from Deck
    private void updateFromDeck(Message message){

        //Identifies goal message

        //If a hand is sent with no context
        if(message.getContent() instanceof Hand)
            if(message.getContext() == null)
                //It must be the player's own hand
                setHand(((Message<Hand>)message).getContent());
    }


}
