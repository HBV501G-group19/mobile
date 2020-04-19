package is.hi.hopon.backend.Models;

import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.time.LocalDateTime;

public class CreateRideDetails {
    private GeoJsonPoint origin;
    private GeoJsonPoint destination;
    private GeoJsonLineString route;
    private int driverId;
    private LocalDateTime departureTime;
    private long duration;
    private short seats;

    public CreateRideDetails(
        int driverId,
        GeoJsonPoint origin,
        GeoJsonPoint destination,
        long duration,
        GeoJsonLineString route,
        LocalDateTime departureTime,
        short seats
    ) {
        this.driverId = driverId;
        this.origin = origin;
        this.destination = destination;
        this.route = route;
        this.departureTime = departureTime;
        this.duration = duration;
        this.seats = seats;
    }

    public int getDriverId() { return driverId; }
    public GeoJsonLineString getRoute() { return route; }
    public GeoJsonPoint getOrigin() { return origin; }
    public GeoJsonPoint getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public short getSeats() { return seats; }
    public long getDuration() { return duration; }
}
