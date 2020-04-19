package is.hi.hopon.ui.message;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.backend.Models.Message;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ConversationListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_USER_ID = "user-id";
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;
    private RecyclerView.Adapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConversationListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConversationListFragment newInstance(int userId) {
        ConversationListFragment fragment = new ConversationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int userId = getArguments().getInt(ARG_USER_ID);
            HoponBackend backend = HoponContext.getInstance().getBackend();
            backend.getUsersConversations(userId, new HoponBackend.HoponBackendResponse<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> response) {
                    updateItems(response);
                }

                @Override
                public void onError(Exception error) {
                    Log.println(Log.DEBUG, "conversation-list-fragment", "error fetching conversationts");
                    Log.println(Log.DEBUG, "conversation-list-fragment", error.getMessage());
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            Log.println(Log.DEBUG, "fragment-making", "making the fragment you bitch");
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ConversationRecyclerViewAdapter(ConversationListContent.CONVERSATIONS, mListener));
            adapter = recyclerView.getAdapter();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateItems(List<Conversation> convos) {
        ConversationListContent.addConversations(convos);
        adapter.notifyDataSetChanged();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Conversation convo);
    }
}
