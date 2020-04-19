package is.hi.hopon.backend.deserialize;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.backend.Models.Message;

public class ConversationDeserializer implements JsonDeserializer<Conversation> {
    public Conversation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject m = json.getAsJsonObject();
        int id = m.getAsJsonPrimitive("conversationId").getAsInt();
        int rideId = m.getAsJsonPrimitive("rideId").getAsInt();
        boolean isDriver = m.getAsJsonPrimitive("driver").getAsBoolean();

        JsonArray messagesJson = m.getAsJsonArray("messages");
        List<Message> messages = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement messageJson : messagesJson) {
            messages.add(gson.fromJson(messageJson, Message.class));
        }

        JsonArray userIdsJson = m.getAsJsonArray("userIds");
        List<Integer> userIds = new ArrayList<>();
        for (JsonElement userIdJson : userIdsJson) {
            int userId = userIdJson.getAsJsonPrimitive().getAsInt();
            userIds.add(userId);

        }
        return new Conversation(
                id,
                rideId,
                isDriver,
                messages,
                userIds
        );
    }
}
