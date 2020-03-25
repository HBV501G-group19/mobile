package is.hi.hopon.ui.message;

import java.util.UUID;

/*
    This is a concrete class for messages.
 */

public class MessageEntity {
    private UUID mId;
    private String mMessage;

    public MessageEntity(String message){
        mId = UUID.randomUUID();
        mMessage = message;
    }

    public UUID getId(){
        return mId;
    }

    public String getCurrentMessage(){
        return mMessage;
    }
}
