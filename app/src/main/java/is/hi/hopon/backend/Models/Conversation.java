package is.hi.hopon.backend.Models;

import java.util.List;

public class Conversation {
    private int conversationId;
    private int rideId;
    private boolean driver;
    private List<Message> messages;
    private List<Integer> userIds;

    public Conversation(
            int id,
            int ride,
            boolean isDriver,
            List<Message> messageList,
            List<Integer> users
    ) {
        conversationId = id;
        rideId = ride;
        driver = isDriver;
        messages = messageList;
        userIds = users;
    }

    public int getConversationId() { return conversationId; }
    public int getRideId() { return rideId; }
    public boolean isDriver() { return driver; }
    public List<Message> getMessages() { return messages; }
    public List<Integer> getUserIds() { return userIds; }
}
