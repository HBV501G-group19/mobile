package is.hi.hopon.ui.message;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import is.hi.hopon.R;

/*
    This is the main class for the message board.
 */

public class MessageBoardActivity extends MultipleFragmentsActivity
        implements MessageCreationFragment.Callbacks{

    private static UUID mUUID;

    // This method fires up the activity, it is called from
    // the SectionsPagerAdapter class in the ui.main package.
    public static Intent newIntent(Context packageContext, UUID uuid){
        Intent intent = new Intent(packageContext, MessageBoardActivity.class);
        mUUID = uuid;
        return intent;
    }

    // Telling the MessageListingFragment to display the new message.
    @Override
    public void onViewUpdate() {
        MessageListingFragment listFragment = (MessageListingFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container_large);

        listFragment.updateUI();
    }

    // Connecting the fragments to their respective frame;
    @Override
    public Map<Integer, Fragment> addFragments() {
        Map<Integer, Fragment> fragments = new HashMap<>();
        fragments.put(R.id.fragment_container_small, MessageCreationFragment.newInstance());
        fragments.put(R.id.fragment_container_large, MessageListingFragment.newInstance());

        return fragments;
    }
}