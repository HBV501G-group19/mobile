package is.hi.hopon.backend.Models.Login;

import androidx.transition.Visibility;

import java.util.List;

import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Ride;

public class UserDetails extends Model {
    @Model.Field
    private Integer id;

    @Model.Field
    private String username;

    @Model.Field
    private List<Ride> drives;

    @Model.Field
    private List<Ride> rides;

    public Integer getId() { return id; }


    public String getUsername() { return username; }

    public List<Ride> getDrives() { return drives; }

    public List<Ride> getRides() { return rides; }
}
