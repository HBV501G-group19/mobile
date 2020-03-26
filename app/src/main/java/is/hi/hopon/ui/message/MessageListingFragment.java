package is.hi.hopon.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.hopon.R;

/*
    This fragment receives messages from MessageLab.
    It uses a recyclerview to handle the amount of messages.
 */

public class MessageListingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MessageAdapter mMessageAdapter;
    private RecyclerView.LayoutManager mManager;

    public static MessageListingFragment newInstance() {
        return new MessageListingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_listing, container, false);
        mRecyclerView = view.findViewById(R.id.message_list_recycle_view);
        mManager = new LinearLayoutManager(getActivity());

        updateUI();

        return view;
    }

    // Calling this method tells the fragment to
    // update the message list in the recyclerview.
    public void updateUI() {
        MessageLab messageLab = MessageLab.get();
        List<MessageEntity> messages =  messageLab.getMessages();

        if (mMessageAdapter == null) {
            mMessageAdapter = new MessageAdapter(messages);
            mRecyclerView.setLayoutManager(mManager);
            mRecyclerView.setAdapter(mMessageAdapter);
        } else {
            mMessageAdapter.setMessages(messages);
        }

        mMessageAdapter.notifyDataSetChanged();
    }

    // This class is a handler of TextView wiring.
    // It holds on to the view.
    private class MessageHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public MessageHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.message_list_item_text_view);
        }

        // This is a helper method to bind a holder to a data.
        public void bindMessage(MessageEntity message){
            mTextView.setText(message.getCurrentMessage());
        }
    }

    // This class creates the necessary MessageHolders and then
    // calls the binding method of the MessageHolder class so
    // the holders can be bound to some data.
    private class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{
        private List<MessageEntity> mMessages;

        public MessageAdapter(List<MessageEntity> messages){
            mMessages = messages;
        }

        @NonNull
        @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_message_listing, parent, false);

            return new MessageHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
            MessageEntity message = mMessages.get(position);
            holder.bindMessage(message);
        }

        @Override
        public int getItemCount() {
            // This needs to be a non-zero number, else nothing appears on the screen.
            return mMessages.size();
        }

        public void setMessages(List<MessageEntity> messages) {
            mMessages = messages;
        }
    }
}
