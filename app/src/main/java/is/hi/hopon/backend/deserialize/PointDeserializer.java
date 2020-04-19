package is.hi.hopon.backend.deserialize;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;

public class PointDeserializer implements JsonDeserializer<GeoJsonPoint> {
    public GeoJsonPoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject p = json.getAsJsonObject();
        JsonArray c = p.getAsJsonArray("coordinates");
        Double[] coords = new Double[2];
        int i = 0;
        for (JsonElement d : c) {
            coords[i] = d.getAsDouble();
            i++;
        }

        LatLng latLng = new LatLng(coords[1], coords[0]);
        return new GeoJsonPoint(latLng);
    }
}
