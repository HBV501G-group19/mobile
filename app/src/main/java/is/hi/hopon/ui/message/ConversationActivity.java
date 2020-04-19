package is.hi.hopon.ui.message;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.backend.Models.Message;
import is.hi.hopon.backend.Models.Ride;

public class ConversationActivity extends FragmentActivity implements MessageListFragment.OnListFragmentInteractionListener {
    public static final String intentConvoKey = "conversation-activity-convo-cache-key";
    public static final String intentRideKey = "conversation-activity-ride-cache-key";

    private Conversation mConversation;
    private Ride mRide;
    private MessageListFragment mMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mMessageList = (MessageListFragment) getSupportFragmentManager().findFragmentById(R.id.conversation_activity_message_list);

        String convoKey = getIntent().getStringExtra(intentConvoKey);
        mConversation = (Conversation) HoponContext.getInstance().popFromCache(convoKey);

        String rideKey = getIntent().getStringExtra(intentRideKey);
        if (rideKey == null) {
            HoponContext.getInstance().getBackend().getRide(mConversation.getRideId(), new HoponBackend.HoponBackendResponse<Ride>() {
                @Override
                public void onSuccess(Ride response) {
                    mRide = response;

                    TextView isDrivingView = findViewById(R.id.isDriving);
                    Boolean isDriving = HoponContext.getInstance().getUser().getId() == mRide.getDriver();
                    if (isDriving) {
                        isDrivingView.setText(R.string.ride_youre_driving);
                    } else {
                        isDrivingView.setText("not you" + " is driving");
                    }

                    TextView originView = findViewById(R.id.origin);
                    TextView destinationView = findViewById(R.id.destination);

                    originView.setText(getPlaceName((Map<Object, Object>) mRide.getProperties().get("origin")));
                    destinationView.setText(getPlaceName((Map<Object, Object>) mRide.getProperties().get("destination")));
                }

                @Override
                public void onError(Exception error) {
                    Log.println(Log.DEBUG, "conversation-activity", "error fetching ride");
                    Log.println(Log.DEBUG, "conversation-activity", error.getMessage());
                }
            });
        } else {
            HashMap<String, Object> c = HoponContext.getInstance().getCache();
            for (Object o : c.values()) {
                Log.println(Log.DEBUG, "convo-activity", String.valueOf(o.getClass()));
            }
            for (String o : c.keySet()) {
                Log.println(Log.DEBUG, "convo-activity", o + " | " + rideKey);
            }
            Ride r = (Ride) HoponContext.getInstance().popFromCache(rideKey);
            mRide = r;
            TextView isDrivingView = findViewById(R.id.isDriving);
            Boolean isDriving = HoponContext.getInstance().getUser().getId() == mRide.getDriver();
            if (isDriving) {
                isDrivingView.setText(R.string.ride_youre_driving);
            } else {
                isDrivingView.setText("not you" + " is driving");
            }

            TextView originView = findViewById(R.id.origin);
            TextView destinationView = findViewById(R.id.destination);

            originView.setText(getPlaceName((Map<Object, Object>) mRide.getProperties().get("origin")));
            destinationView.setText(getPlaceName((Map<Object, Object>) mRide.getProperties().get("destination")));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMessageList.updateItems(mConversation.getMessages());
    }

    @Override
    public void onListFragmentInteraction(Message message) {

    }

    private String getPlaceName(Map<Object, Object> map) {
        String name = (String) map.get("name");
        String street = (String) map.get("street");

        return name != null ? name : street;
    }
}
