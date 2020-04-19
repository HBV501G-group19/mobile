package is.hi.hopon.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.hopon.R;
import is.hi.hopon.backend.Models.Message;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {
    private final List<Message> messages;
    private final MessageListFragment.OnListFragmentInteractionListener mListener;

    public MessageRecyclerViewAdapter(List<Message> messages, MessageListFragment.OnListFragmentInteractionListener listener) {
        this.messages = messages;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new MessageRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.msg = message;
        holder.mSender.setText(message.getSenderName());
        holder.mBody.setText(message.getBody());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSender;
        public final TextView mBody;
        public Message msg;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSender = (TextView) view.findViewById(R.id.message_sender);
            mBody = (TextView) view.findViewById(R.id.message_body);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSender.getText() + "'" + mBody.getText() + "'";
        }
    }
}
