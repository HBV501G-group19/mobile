package is.hi.hopon.backend.Models;

public class Message {
    private String body;
    private int id;
    private int senderId;
    private String senderName;
    private int recipientId;
    private String recipientName;
    private int rideId;
    private int conversationId;

    /*
    {
    "id": 1,
    "body": "hello",
    "recipient": 1,
    "sender": 2,
    "ride": 2,
    "conversationId": -981530759,
    "recipientName": "gunnar",
    "senderName": "ekkigunnar"
}
     */
    public Message(int id, int sender, String senderName, int recipient, String recipientName, int ride, int conversation, String body) {
        this.id = id;
        senderId = sender;
        this.senderName = senderName;
        recipientId = recipient;
        this.recipientName = recipientName;
        rideId = ride;
        this.body = body;
        this.conversationId = conversation;
    }

    public int getId() { return id; }
    public String getBody() { return body; }
    public int getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public int getRecipientId() { return recipientId; }
    public String getRecipientName() { return recipientName; }
    public int getConversationId() { return conversationId; }
    public int getRideId() { return rideId; }
}
