package is.hi.hopon.backend.deserialize;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.maps.android.data.geojson.GeoJsonLineString;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LineStringDeserializer implements JsonDeserializer<GeoJsonLineString> {
    public GeoJsonLineString deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Log.println(Log.DEBUG, "linestring-des", json.toString());
        JsonObject p = json.getAsJsonObject();
        JsonArray coordinatesListJson = p.getAsJsonArray("coordinates");
        List<LatLng> coordinatesList = new ArrayList<>();
        for (JsonElement c : coordinatesListJson) {
            JsonArray coordinatesJson = c.getAsJsonArray();
            Double[] coords = new Double[2];
            int i = 0;
            for (JsonElement d : coordinatesJson) {
                coords[i] = d.getAsDouble();
                i++;
            }

            LatLng latLng = new LatLng(coords[1], coords[0]);
            coordinatesList.add(latLng);
        }

        return new GeoJsonLineString(coordinatesList);
    }
}
