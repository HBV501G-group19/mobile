package is.hi.hopon.backend.Models;

import com.google.gson.Gson;

import java.util.List;

public class User {
    private int id;
    private String username;
    private List<Ride> drives;
    private List<Ride> rides;

    public User(
        int id,
        String username,
        List<Ride> drives,
        List<Ride> rides
    ) {
        this.id = id;
        this.username = username;
        this.drives = drives;
        this.rides = rides;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Ride> getRides() { return rides; }
    public List<Ride> getDrives() { return drives; }
}
