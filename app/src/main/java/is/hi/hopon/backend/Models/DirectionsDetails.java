package is.hi.hopon.backend.Models;

import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.util.HashMap;

public class DirectionsDetails {
    /*
    {
        "origin": {
            "type": "Point",
        ...
        },
        "destination": {
            "type": "Point",
        ...
        },
        "properties": {
            "profile": "foot-walking"|"driving-car"
        }
    }
     */

    private GeoJsonPoint origin;
    private GeoJsonPoint destination;
    private HashMap<String, String> properties;

    public DirectionsDetails(
            GeoJsonPoint origin,
            GeoJsonPoint destination,
            HashMap<String, String> properties
    ) {
        this.origin = origin;
        this.destination = destination;
        this.properties = properties;
    }

    public GeoJsonPoint getOrigin() { return origin; }
    public GeoJsonPoint getDestination() { return destination; }
    public HashMap<String, String> getProperties() { return properties; }
}
