package is.hi.hopon.ui.message;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import is.hi.hopon.backend.Models.Message;

public class MessageListContent {
    public static final List<Message> MESSAGES = new ArrayList<>();

    public static void addMessages(List<Message> messages) {
        MESSAGES.addAll(messages);
        Log.println(Log.DEBUG, "list-updated", "added");
    }

    public static void addMessage(Message message) {
        MESSAGES.add(message);
        Log.println(Log.DEBUG, "list-updated", "added");
    }
}
