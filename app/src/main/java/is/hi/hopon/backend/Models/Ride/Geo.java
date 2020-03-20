package is.hi.hopon.backend.Models.Ride;

import java.util.List;

import is.hi.hopon.backend.Models.Core.Model;

public class Geo extends Model {
    @Model.Field(name="type")
    private String geoType;

    @Model.Field
    private List<Double> coordinates;

    public String getGeoType() {
        return geoType;
    }
    public void setGeoType(String geoType){
        this.geoType = geoType;
    }

    public List<Double> getCoordinates()
    {
        return coordinates;
    }
    public void setCoordinates(List<Double> coordinates)
    {
        this.coordinates = coordinates;
    }
}
