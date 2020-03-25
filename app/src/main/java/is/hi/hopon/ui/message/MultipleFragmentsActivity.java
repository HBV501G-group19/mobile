package is.hi.hopon.ui.message;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Map;
import java.util.Objects;

import is.hi.hopon.R;

/*
    This class inflates the given fragment layouts for the message board.
 */

public abstract class MultipleFragmentsActivity extends FragmentActivity {

    public abstract Map<Integer, Fragment> addFragments();

    @LayoutRes
    private int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();

        for(Integer key : addFragments().keySet()){
            if(fm.findFragmentById(key) == null) {
                fm.beginTransaction()
                        .add(key, Objects.requireNonNull(addFragments().get(key)))
                        .commit();
            }
        }
    }
}