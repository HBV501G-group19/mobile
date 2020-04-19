package is.hi.hopon.ui.message;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import is.hi.hopon.backend.Models.Conversation;

public class ConversationListContent {
    public static final List<Conversation> CONVERSATIONS = new ArrayList<>();

    public static void addConversations(List<Conversation> convos) {
        CONVERSATIONS.addAll(convos);
        Log.println(Log.DEBUG, "list-updated", "added");
    }

    public static void addConversation(Conversation convo) {
        CONVERSATIONS.add(convo);
        Log.println(Log.DEBUG, "list-updated", "added");
    }
}
