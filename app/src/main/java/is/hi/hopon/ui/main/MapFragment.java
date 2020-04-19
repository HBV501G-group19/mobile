package is.hi.hopon.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import is.hi.hopon.HoponContext;
import is.hi.hopon.R;
import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.CreateRideDetails;
import is.hi.hopon.backend.Models.DirectionsDetails;
import is.hi.hopon.backend.Models.DirectionsResultList;
import is.hi.hopon.backend.Models.DirectionsResults;
import is.hi.hopon.backend.Models.Geocode;
import is.hi.hopon.backend.Models.GeocodeResult;
import is.hi.hopon.backend.Models.Ride;
import is.hi.hopon.ui.ride.RideActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private FusedLocationProviderClient fusedLocationClient; // takes care of users location, might possible want this in a higher scope than the map
    private GoogleMap mGoogleMap;                            // the actual rendered map
    private View mView;                                    // generic view component that the fragment gets inflated into
    private GeoJsonPoint mLocation;
    SupportMapFragment mapFragment;

    private GeocodeResult originResults;
    private MarkerOptions originMarker;
    private GeocodeResult destinationResults;
    private MarkerOptions destinationMarker;
    private PolylineOptions route;

    private DirectionsResults mDirections;

    private HashMap<String, String> directionsPropertiesHashMap = new HashMap<>();

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directionsPropertiesHashMap.put("profile", "driving-car");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        Button submit = getView().findViewById(R.id.main_submit);
        Button sendOrigin = (Button) getView().findViewById(R.id.send_origin);
        Button sendDestination = (Button) getView().findViewById(R.id.send_destination);

        TextInputEditText seats = (TextInputEditText) getView().findViewById(R.id.seats_value);
        seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
                params.height = 0;

                mapFragment.getView().setLayoutParams(params);
            }
        });

        sendOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText input = (TextInputEditText) getView().findViewById(R.id.origin_value);
                String geocodeString = input.getText().toString().trim();
                input.setEnabled(false);

                if (geocodeString == "") return;

                fusedLocationClient.getLastLocation();
                Geocode geocode = new Geocode(mLocation, geocodeString);
                try {
                    HoponContext.getInstance().getBackend().getGeocode(geocode, new HoponBackend.HoponBackendResponse<GeocodeResult>() {
                        @Override
                        public void onSuccess(GeocodeResult response) {
                            updateOrigin(response);
                            input.setEnabled(true);
                            originResults = response;
                        }

                        @Override
                        public void onError(Exception error) {
                            Log.println(Log.ERROR, "origin-geocode-error", error.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.println(Log.ERROR, "origin-geocode-json-error", e.getMessage());
                }
            }
        });

        sendDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText input = (TextInputEditText) getView().findViewById(R.id.destination_value);
                String geocodeString = input.getText().toString().trim();
                input.setEnabled(false);

                if (geocodeString == "") return;

                fusedLocationClient.getLastLocation();
                Geocode geocode = new Geocode(mLocation, geocodeString);
                try {
                    HoponContext.getInstance().getBackend().getGeocode(geocode, new HoponBackend.HoponBackendResponse<GeocodeResult>() {
                        @Override
                        public void onSuccess(GeocodeResult response) {
                            updateDestination(response);
                            destinationResults = response;
                            input.setEnabled(true);
                        }

                        @Override
                        public void onError(Exception error) {
                            Log.println(Log.ERROR, "destination-geocode-error", error.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.println(Log.ERROR, "destination-geocode-json-error", e.getMessage());
                }
            }
        });

        Button needsRide = (Button) getView().findViewById(R.id.get_ride_button);
        Button createRide = (Button) getView().findViewById(R.id.create_ride_button);

        needsRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRide.setEnabled(true);
                seats.setEnabled(false);
                v.setEnabled(false);
                // send request
                // start searchResult activity
            }
        });

        createRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needsRide.setEnabled(true);
                seats.setEnabled(true);
                v.setEnabled(false);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setEnabled(false);
                        HoponBackend backend = HoponContext.getInstance().getBackend();

                        GeoJsonFeature route = mDirections.getPath();
                        long duration = 0;

                        for (Object property : route.getProperties()) {
                            Log.println(Log.DEBUG, "properties-classes", String.valueOf(property.getClass()));
                        }
                        CreateRideDetails createRide = new CreateRideDetails(
                                HoponContext.getInstance().getUser().getId(),
                                new GeoJsonPoint(originMarker.getPosition()),
                                new GeoJsonPoint(destinationMarker.getPosition()),
                                0,
                                null,
                                null,
                                (short)0
                        );

                        try {
                            backend.createRideRequest(createRide, new HoponBackend.HoponBackendResponse<Ride>() {
                                @Override
                                public void onSuccess(Ride response) {
                                    String cacheKey = "new-created-ride";
                                    Intent intent = new Intent(getContext(), RideActivity.class);
                                    HoponContext.getInstance().addToCache(cacheKey, response);
                                    intent.putExtra(RideActivity.RIDE_CACHE_KEY, cacheKey);
                                }

                                @Override
                                public void onError(Exception error) {
                                    Log.println(Log.ERROR, "create-ride-response-error", error.getMessage());
                                }
                            });
                        } catch (JSONException e) {
                            Log.println(Log.ERROR, "create-ride-json-response-error", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fullscreen, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Context context = getContext();
        MapsInitializer.initialize(context);

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // to use these permissions we need to have them mentioned in AndroidManifest.xml
            // this should be dealt with on startup
            requestLocation();
        } else {
            moveLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            moveLocation();
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        String[] permissions = new String[2];
        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

        requestPermissions(permissions, 0);
    }

    private void moveLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Add a marker on users location and move the camera
                            LatLng position;
                            position = new LatLng(location.getLatitude(), location.getLongitude());

                            mLocation = new GeoJsonPoint(position);
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println(e.getMessage());
                    }
                });
    }

    private void updateOrigin(GeocodeResult features) {
        GeoJsonFeature feature = getHighestConfidenceFeature(features);
        GeoJsonPoint point = (GeoJsonPoint) feature.getGeometry();
        LatLng position = point.getCoordinates();
        mGoogleMap.clear();
        originMarker = new MarkerOptions().position(position);
        mGoogleMap.addMarker(originMarker);

        if (destinationMarker == null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        } else {
            mGoogleMap.addMarker(destinationMarker);
            LatLng originPosition = destinationMarker.getPosition();
            LatLng destinationPosition = originMarker.getPosition();
            LatLng first = originPosition.latitude < destinationPosition.latitude ? originPosition : destinationPosition;
            LatLng second = originPosition.latitude > destinationPosition.latitude ? originPosition : destinationPosition;

            LatLngBounds bounds = new LatLngBounds(first, second);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));

            DirectionsDetails details = new DirectionsDetails(
                    new GeoJsonPoint(originPosition),
                    new GeoJsonPoint(destinationPosition),
                    directionsPropertiesHashMap
            );
            try {
                HoponContext.getInstance().getBackend().getDirections(details, new HoponBackend.HoponBackendResponse<DirectionsResultList>() {
                    @Override
                    public void onSuccess(DirectionsResultList response) {
                        DirectionsResults directions = response.results.get(0);
                        GeoJsonFeature path = directions.getPath();
                        GeoJsonLineString lineString = (GeoJsonLineString) path.getGeometry();

                        route = new PolylineOptions();
                        route.addAll(lineString.getCoordinates());
                        mGoogleMap.addPolyline(route);
                        mDirections = directions;
                    }

                    @Override
                    public void onError(Exception error) {
                        Log.println(Log.ERROR, "json-error-directions-response", error.getMessage());
                    }
                });
            } catch (JSONException e) {
                Log.println(Log.ERROR, "json-error-directions-response", e.getMessage());
            }
        }
    }
    private void updateDestination(GeocodeResult features) {
        GeoJsonFeature feature = getHighestConfidenceFeature(features);
        GeoJsonPoint point = (GeoJsonPoint) feature.getGeometry();
        LatLng position = point.getCoordinates();
        mGoogleMap.clear();
        destinationMarker = new MarkerOptions().position(position);
        mGoogleMap.addMarker(destinationMarker);

        if (originMarker == null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
        } else {
            mGoogleMap.addMarker(originMarker);
            LatLng originPosition = originMarker.getPosition();
            LatLng destinationPosition = destinationMarker.getPosition();
            LatLng first = originPosition.latitude < destinationPosition.latitude ? originPosition : destinationPosition;
            LatLng second = originPosition.latitude > destinationPosition.latitude ? originPosition : destinationPosition;

            LatLngBounds bounds = new LatLngBounds(first, second);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));


            DirectionsDetails details = new DirectionsDetails(
                    new GeoJsonPoint(originPosition),
                    new GeoJsonPoint(destinationPosition),
                    directionsPropertiesHashMap
            );
            try {
                HoponContext.getInstance().getBackend().getDirections(details, new HoponBackend.HoponBackendResponse<DirectionsResultList>() {
                    @Override
                    public void onSuccess(DirectionsResultList response) {
                        DirectionsResults directions = response.results.get(0);
                        GeoJsonFeature path = directions.getPath();
                        GeoJsonLineString lineString = (GeoJsonLineString) path.getGeometry();

                        route = new PolylineOptions();
                        route.addAll(lineString.getCoordinates());
                        mGoogleMap.addPolyline(route);
                        mDirections = directions;
                    }

                    @Override
                    public void onError(Exception error) {
                        Log.println(Log.ERROR, "json-error-directions-response", error.getMessage());
                    }
                });
            } catch (JSONException e) {
                Log.println(Log.ERROR, "json-error-directions-response", e.getMessage());
            }
        }
    }

    private GeoJsonFeature getHighestConfidenceFeature(GeocodeResult geoProps) {
        List<GeoJsonFeature> features = geoProps.getFeatures();
        if (features.size() < 1) return null;

        double highestConf = 0;
        GeoJsonFeature highestConfFeature = features.get(0);
        for (GeoJsonFeature feature : features) {
            for(Object propertyObj : feature.getProperties()) {
                Map.Entry<String, String> property = (Map.Entry<String, String>) propertyObj;
                if(property.getKey() == "confidence") {
                    if (Double.parseDouble(property.getValue()) > highestConf) {
                        highestConfFeature = feature;
                    }
                }
            }
        }

        return highestConfFeature;
    }
}