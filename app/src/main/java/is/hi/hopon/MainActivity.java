package is.hi.hopon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.ui.authentication.login.LoginActivity;
import is.hi.hopon.ui.main.SectionsPagerAdapter;
import is.hi.hopon.ui.message.ConversationActivity;
import is.hi.hopon.ui.message.ConversationListFragment;

public class MainActivity extends AppCompatActivity implements ConversationListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HoponContext.getInstance(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }


    @Override
    public void onListFragmentInteraction(Conversation convo) {
        Log.println(Log.DEBUG, "list-interaction", String.valueOf(convo.getConversationId()));
        String cacheKey = "open-conversation-intent";
        HoponContext.getInstance().addToCache(cacheKey, convo);
        Intent openConversationIntent = new Intent(this, ConversationActivity.class);
        openConversationIntent.putExtra(ConversationActivity.intentConvoKey, cacheKey);

        startActivity(openConversationIntent);
    }
}