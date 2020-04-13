package is.hi.hopon.backend.Models;

import android.graphics.Point;

import java.util.Date;
import java.util.List;

public class Ride {
    private Integer driver;
    private String departure_time;
    private Integer duration;
    private Point origin;
    private Point destination;
    private Integer seats;
    private List<Integer> passengers;

    public Ride(Integer driver, String departure_time, Integer duration, Point origin, Point destination, Integer seats, List<Integer> passengers)
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

    public Point getOrigin() {
        return origin;
    }

    public Point getDestination() {
        return destination;
    }

    public Integer getSeats() {
        return seats;
    }

    public List<Integer> getPassengers() {
        return passengers;
    }
}
