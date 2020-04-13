package is.hi.hopon.backend.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Ride {

    private Integer driver;
    private String departure_time;
    private Integer duration;
    private LatLng origin;
    private LatLng destination;
    private Integer seats;
    private List<Integer> passengers;

    public Ride(Integer driver, String departure_time, Integer duration, LatLng origin, LatLng destination, List<LatLng> route, Integer seats, List<Integer> passengers)
    {
        this.driver = driver;
        this.departure_time = departure_time;
        this.duration = duration;
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
        this.passengers = passengers;
    }


    public Integer getDriver() {
        return driver;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public Integer getDuration() {
        return duration;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public Integer getSeats() {
        return seats;
    }

    public List<Integer> getPassengers() {
        return passengers;
    }
}
