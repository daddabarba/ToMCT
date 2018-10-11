package ToMCT.Model.Messages;

import java.util.Observable;

public class Message<T> {

    private T content;
    private Object context;

    private Observable sender;

    public Message(Observable sender, T content){
        this(sender, content, null);
    }

    public Message(Observable sender, T content, Object context){
        this.content = content;
        this.context = context;

        this.sender = sender;
    }

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
