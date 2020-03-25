package is.hi.hopon.ui.message;

import java.util.ArrayList;
import java.util.List;

/*
    This class is a container and provider of messages
    which are created in a session. It gets the messages
    from the supplying fragment and adds them in a list,
    then sends the messages to the receiving fragment.

    This is a singleton class.
 */

public class MessageLab{
    private static MessageLab sLab;
    private List<MessageEntity> mMessages;

    public static MessageLab get(){
        if(sLab == null){
            sLab = new MessageLab();
        }

        return sLab;
    }

    public List<MessageEntity> getMessages(){
        return mMessages;
    }

    // This method should be called when new messages should be added to the collection.
    public void addMessage(MessageEntity message){
        mMessages.add(message);
    }

    // A private constructor.
    private MessageLab(){
        mMessages = new ArrayList<>();
    }
}