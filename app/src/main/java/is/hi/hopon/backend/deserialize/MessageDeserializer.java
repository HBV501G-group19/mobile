package is.hi.hopon.backend.deserialize;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;

import is.hi.hopon.backend.Models.Message;

public class MessageDeserializer implements JsonDeserializer<Message> {
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject m = json.getAsJsonObject();
        int id = m.getAsJsonPrimitive("id").getAsInt();
        int rideId = m.getAsJsonPrimitive("rideId").getAsInt();
        int senderId = m.getAsJsonPrimitive("senderId").getAsInt();
        int recipientId = m.getAsJsonPrimitive("recipientId").getAsInt();
        int conversationId = m.getAsJsonPrimitive("conversationId").getAsInt();

        String body = m.getAsJsonPrimitive("body").getAsString();
        String sender = m.getAsJsonPrimitive("senderName").getAsString();
        String recipient = m.getAsJsonPrimitive("recipientName").getAsString();
// int id, int sender, String senderName, int recipient, String recipientName, int ride, int conversation, String body
        return new Message(
                id,
                senderId,
                sender,
                recipientId,
                recipient,
                rideId,
                conversationId,
                body
        );
    }
}
