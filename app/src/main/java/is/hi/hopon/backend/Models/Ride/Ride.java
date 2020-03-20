package is.hi.hopon.backend.Models.Ride;

import java.util.Date;
import java.util.List;

import is.hi.hopon.backend.Models.Core.Model;

public class Ride extends Model {
    @Model.Field
    private Integer id;

    @Model.Field
    private Integer driver;

    @Model.Field
    private List<Integer> passengers;

    @Model.Field
    private Date created;

    @Model.Field
    private Geo origin;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDriver() {
        return driver;
    }
    public void setDriver(Integer driver) {
        this.driver = driver;
    }

    public Geo getOrigin()
    {
        return origin;
    }

    public void setOrigin(Geo origin)
    {
        this.origin = origin;
    }


    public List<Integer> getPassengers() {
        return passengers;
    }
    public void setPassengers(List<Integer> passengers) {
        this.passengers = passengers;
    }

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
}
