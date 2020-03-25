package is.hi.hopon.ui.message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import is.hi.hopon.R;

/*
    This fragment supplies MessageLab with new messages.
    It has a text area for writing and button for submitting.
 */

public class MessageCreationFragment extends Fragment {

    // This interface is used to ask the hosting activity
    // to inform the other fragment to update it self.
    public interface Callbacks{
        void onViewUpdate();
    }

    private Callbacks mCallbacks;

    private EditText mEditText;
    private Button mButton;

    public static MessageCreationFragment newInstance() {
        return new MessageCreationFragment();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_creation, container, false);

        return writeMessage(view);
    }

    @Override
    public void onDetach(){
        super.onDetach();

        mCallbacks = null;
    }

    // This method encapsulated the writing interface.
    private View writeMessage(View view){
        mEditText = view.findViewById(R.id.message_board_edit_text);

        mButton = view.findViewById(R.id.message_board_button);
        mButton.setOnClickListener(v -> {
            MessageEntity message = new MessageEntity(mEditText.getText().toString());
            MessageLab.get().addMessage(message);

            mCallbacks.onViewUpdate();

            mEditText.getText().clear();
            mEditText.clearFocus();
        });

        return view;
    }
}
