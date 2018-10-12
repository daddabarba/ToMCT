package ToMCT.Model.Messages;

import java.util.Observable;

public class Message<T> {
    //Class wrapping messages sent via notification

    private T content;  //Message sent (actual content)
    private Object context; //Context for message (to differentiate same content types)

    private Observable sender; //Sender of message

    //CONSTRUCTORS

    //Specify content and sender
    public Message(Observable sender, T content){
        this(sender, content, null);
    }

    //Specify content, sender, and context
    public Message(Observable sender, T content, Object context){
        this.content = content;
        this.context = context;

        this.sender = sender;
    }

    //GETTERS

    public T getContent(){
        return content;
    }

    public Object getContext(){
        return context;
    }

    public Observable getSender(){
        return sender;
    }
}
