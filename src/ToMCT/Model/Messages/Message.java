package ToMCT.Model.Messages;

public class Message<T> {

    private T content;
    private Object context;

    public Message(T content){
        this(content, null);
    }

    public Message(T content, Object context){
        this.content = content;
        this.context = context;
    }
}
