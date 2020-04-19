package is.hi.hopon.ui.ride;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.backend.Models.Ride;
import is.hi.hopon.backend.Models.User;
import is.hi.hopon.ui.message.ConversationActivity;
import is.hi.hopon.ui.message.ConversationListFragment;

public class RideActivity extends FragmentActivity implements OnMapReadyCallback, ConversationListFragment.OnListFragmentInteractionListener {
    public static final String RIDE_CACHE_KEY = "ride-activity-cache-key";
    // TODO: setup mapview
    // TODO: create conversation list fragment - fetches convos, open conversation activity on tap, etc.
    private final int userId = HoponContext.getInstance().getUser().getId();
    private User driver;
    private List<User> passengers = new ArrayList<>();
    private Ride currentRide;

    private GoogleMap mGoogleMap;
    private ConversationListFragment mConversationList;

    private String getPlaceName(Map<Object, Object> map) {
        String name = (String) map.get("name");
        String street = (String) map.get("street");

        return name != null ? name : street;
    }

    private void updateLayout() {
        boolean isDriving = userId == driver.getId();

        TextView mIsDriving = findViewById(R.id.isDriving);
        if (isDriving) mIsDriving.setText(R.string.ride_youre_driving);
        else mIsDriving.setText(driver.getUsername() + " is driving");

        Map<String, Object> rideProperties = currentRide.getProperties();
        Map<Object, Object> originProperty = (Map<Object, Object>) rideProperties.get("origin");
        String originName = getPlaceName(originProperty);

        Map<Object, Object> destinationProperty = (Map<Object, Object>) rideProperties.get("destination");
        String destinationName = getPlaceName(destinationProperty);

        TextView mOrigin = findViewById(R.id.origin);
        TextView mDestination = findViewById(R.id.destination);

        mOrigin.setText(originName);
        mDestination.setText(destinationName);
        LocalDateTime currentTime = LocalDateTime.now();
        currentRide.getDepartureTime();

        long daysToRide = ChronoUnit.DAYS.between(currentTime, currentRide.getDepartureTime());
        long hoursToRide = ChronoUnit.HOURS.between(currentTime, currentRide.getDepartureTime());
        long mintuesToRide = ChronoUnit.MINUTES.between(currentTime, currentRide.getDepartureTime());

        TextView mMinutes = findViewById(R.id.minutes);
        //TODO: add text for minutes hours and days
        mMinutes.setText(String.valueOf(mintuesToRide));

        try {
            updateMap();
        } catch (JSONException e) {}
    }

    private void updateMap() throws JSONException {
        if (mGoogleMap == null) return;
        if (currentRide == null) return;

        // Add a marker in Sydney and move the camera
        LatLng origin = currentRide.getOrigin().getCoordinates();
        LatLng destination = currentRide.getDestination().getCoordinates();
        LatLngBounds bounds = new LatLngBounds(origin, destination);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));

        try {
            GeoJsonLineString route = currentRide.getRoute();
            GeoJsonFeature routeFeature = new GeoJsonFeature(route, "route", null, bounds);
            GeoJsonLayer layer = new GeoJsonLayer(mGoogleMap, new JSONObject(
                    "{"
                    + "\"type\": \"FeatureCollection\","
                    + "\"features\": []" +
                    "}"
            ));

            GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
            lineStringStyle.setColor(Color.RED);

            routeFeature.setLineStringStyle(lineStringStyle);


            layer.addFeature(routeFeature);
            layer.addFeature(routeFeature);
            layer.addLayerToMap();

        } catch (Exception e) {
            Log.println(Log.DEBUG, "update-map", e.getMessage());
        }
    }

    private void updateConversations(List<Conversation> conversations) {
            mConversationList.updateItems(conversations);
    }

    private void fetchRide(HoponBackend backend) {
        backend.getRide(1,
                new HoponBackend.HoponBackendResponse<Ride>() {
                    @Override
                    public void onSuccess(Ride ride) {
                        currentRide = ride;
                        fetchDriver(backend, ride);
//                        fetchPassengers(backend, ride);
                    }
                    @Override
                    public void onError(Exception error) {
                        Log.println(Log.DEBUG, "ride-request", "failed");
                    }
                });
    }

    private void fetchDriver(HoponBackend backend, Ride ride) {
        backend.getUser(ride.getDriver(), new HoponBackend.HoponBackendResponse<User>() {
            @Override
            public void onSuccess(User user) {
                driver = user;
                fetchConversations(backend, user);
                updateLayout();
            }

            @Override
            public void onError(Exception error) {
                Log.println(Log.DEBUG, "user-request", "failed");
            }
        });
    }

    private void fetchConversations(HoponBackend backend, User user) {
        backend.getUsersConversations(user.getId(), new HoponBackend.HoponBackendResponse<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> response) {
                updateConversations(response);
            }

            @Override
            public void onError(Exception error) {
                Log.println(Log.DEBUG, "user-conversations", "failed");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ride_view_map);
        mapFragment.getMapAsync(this);

        mConversationList = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.ride_view_convolist);

        HoponBackend backend = HoponContext.getInstance().getBackend();

        String rideCacheKey = getIntent().getStringExtra(RIDE_CACHE_KEY);
        if (rideCacheKey == null) {
            fetchRide(backend);
        } else {
            currentRide = (Ride) HoponContext.getInstance().popFromCache(rideCacheKey);
            fetchDriver(backend, currentRide);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Context context = getApplicationContext();
        MapsInitializer.initialize(context);

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            updateMap();
        } catch (JSONException e) {}
    }

    @Override
    public void onListFragmentInteraction(Conversation convo) {
        String convoKey = "conversation-from-ride-activity";
        String rideKey = "ride-from-ride-activity";
        HoponContext.getInstance().addToCache(convoKey, convo);
        HoponContext.getInstance().addToCache(rideKey, currentRide);
        Intent openConversationIntent = new Intent(this, ConversationActivity.class);
        openConversationIntent.putExtra(ConversationActivity.intentConvoKey, convoKey);
        openConversationIntent.putExtra(ConversationActivity.intentRideKey, rideKey);

        startActivity(openConversationIntent);
    }
}
