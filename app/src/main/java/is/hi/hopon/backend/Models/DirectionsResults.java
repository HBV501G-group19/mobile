package is.hi.hopon.backend.Models;

import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.util.List;

public class DirectionsResults {
    private GeoJsonFeature path;
    private List<GeoJsonFeature> points;

    public DirectionsResults(GeoJsonFeature path, List<GeoJsonFeature> points) {
        this.path = path;
        this.points = points;
    }

    public GeoJsonFeature getPath() { return path; }
    public List<GeoJsonFeature> getPoints() { return points; }
}
