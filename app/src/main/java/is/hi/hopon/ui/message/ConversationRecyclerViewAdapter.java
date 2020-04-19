package is.hi.hopon.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.hopon.R;
import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.backend.Models.Message;

public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<ConversationRecyclerViewAdapter.ViewHolder> {
    private final List<Conversation> conversations;
    private final ConversationListFragment.OnListFragmentInteractionListener mListener;

    public ConversationRecyclerViewAdapter(List<Conversation> convos, ConversationListFragment.OnListFragmentInteractionListener listener) {
        conversations = convos;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_conversation_preview, parent, false);
        return new ConversationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversation convo = conversations.get(position);
        List<Message> messages = convo.getMessages();
        Message lastSent = messages.get(messages.size() - 1);
        holder.msg = lastSent;

        holder.mName.setText(lastSent.getSenderName());
        holder.mPreview.setText(lastSent.getBody());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(convo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mPreview;
        public Message msg;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.conversation_name);
            mPreview = (TextView) view.findViewById(R.id.conversation_preview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'" + mName.getText() + "'";
        }
    }
}
