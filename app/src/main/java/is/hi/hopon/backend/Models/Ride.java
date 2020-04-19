package is.hi.hopon.backend.Models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class Ride {

    private Integer driver;
    private LocalDateTime departure;
    private Long duration;
    private GeoJsonPoint origin;
    private GeoJsonPoint destination;
    private GeoJsonLineString route;
    private short freeSeats;
    private short totalSeats;
    private List<Integer> passengers;
    private HashMap<String, Object> properties;

    public Ride(
            Integer driver,
            LocalDateTime departure,
            Long duration,
            GeoJsonPoint origin,
            GeoJsonPoint destination,
            GeoJsonLineString route,
            short freeSeats,
            short totalSeats,
            List<Integer> passengers,
            HashMap<String, Object> properties
        )
    {
        this.driver = driver;
        this.departure = departure;
        this.duration = duration;
        this.origin = origin;
        this.destination = destination;
        this.route = route;
        this.freeSeats = freeSeats;
        this.totalSeats = totalSeats;
        this.passengers = passengers;
        this.properties = properties;
        if (properties != null) Log.println(Log.DEBUG, "ride-properties", properties.toString());
        else Log.println(Log.DEBUG, "ride-properties", "its null " + driver);
    }

    public Integer getDriver() {
        return driver;
    }

    public LocalDateTime getDepartureTime() {
        return departure;
    }

    public Long getDuration() {
        return duration;
    }

    public GeoJsonPoint getOrigin() {
        return origin;
    }

    public GeoJsonPoint getDestination() {
        return destination;
    }

    public GeoJsonLineString getRoute() { return route; }

    public short getFreeSeats() {
        return freeSeats;
    }

    public short getTotalSeats() {
        return totalSeats;
    }

    public List<Integer> getPassengers() {
        return passengers;
    }

    public HashMap<String, Object> getProperties() { return properties; }
}
