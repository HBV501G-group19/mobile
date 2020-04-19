package is.hi.hopon.backend.Models;

import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.util.List;

public class GeocodeResult {
    final String type = "FeatureCollection";
    List<GeoJsonFeature> features;

    public GeocodeResult(List<GeoJsonFeature> features) {
        this.features = features;
    }

    public List<GeoJsonFeature> getFeatures() { return features; }
}
