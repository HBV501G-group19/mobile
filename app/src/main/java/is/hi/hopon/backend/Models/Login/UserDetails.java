package is.hi.hopon.backend.Models.Login;

import androidx.transition.Visibility;

import java.util.List;

import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Ride.Ride;

public class UserDetails extends Model {
    @Model.Field
    private Integer id;

    @Model.Field
    private String username;

    @Model.Field(objectList = Ride.class)
    private List<Ride> rides;


    public Integer getId() { return id; }
    public void setId(Integer id){ this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<Ride>getRides() { return rides; }
    public void setRides(List<Ride> rides) { this.rides = rides; }
}
