package is.hi.hopon.backend.Models;

import com.google.maps.android.data.geojson.GeoJsonPoint;

public class Geocode {
    GeoJsonPoint focus;
    String geocode;

    public Geocode(
            GeoJsonPoint focus,
            String geocode
    ) {
        this.focus = focus;
        this.geocode = geocode;
    }

    public String getGeocode() { return geocode; }
    public GeoJsonPoint getFocus() { return focus; }
}
