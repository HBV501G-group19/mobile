package is.hi.hopon.ui.ride;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import is.hi.hopon.R;

public class rideActivity extends AppCompatActivity {

    // TODO: get info dynamically from backend(requires networking stuff to be merged)
    // TODO: setup mapview
    // TODO: create conversation list fragment - fetches convos, open conversation activity on tap, etc.

    private boolean isDriving = true;
    private String driverName = "Gunnar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        TextView mIsDriving = findViewById(R.id.isDriving);
        if (isDriving) mIsDriving.setText(R.string.ride_youre_driving);
        else mIsDriving.setText(driverName + " is driving");

        TextView mOrigin = findViewById(R.id.origin);
        TextView mDestination = findViewById(R.id.destination);

        mOrigin.setText("Laugardalur");
        mDestination.setText("Háskóli Íslands");

        TextView mMinutes = findViewById(R.id.minutes);
        mMinutes.setText("15");
    }
}
