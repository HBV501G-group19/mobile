package is.hi.hopon.ui.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.ui.message.ConversationListFragment;
import is.hi.hopon.ui.message.MessageBoardActivity;
import is.hi.hopon.ui.main.PlaceholderFragment;
import is.hi.hopon.ui.ride.RideListFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.page_rides, R.string.page_map, R.string.page_messages};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position) {
            case 0: return RideListFragment.newInstance(0);
            case 1: return MapFragment.newInstance();
            case 2: return ConversationListFragment.newInstance(HoponContext.getInstance().getUser().getId());
//                    Intent intent = MessageBoardActivity.newIntent(mContext.getApplicationContext(),
//                            HoponContext.getInstance().getUser().getUUID());
//
//                    mContext.startActivity(intent);
//                return  PlaceholderFragment.newInstance(1);
        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}