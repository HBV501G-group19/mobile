package is.hi.hopon.backend.serialize;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;

public class LineStringSerializer implements JsonSerializer<GeoJsonLineString> {
    public JsonElement serialize(GeoJsonLineString src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getType());
        JsonArray coords = new JsonArray();
        for (LatLng latLng : src.getCoordinates()) {
            JsonArray coord = new JsonArray();
            coord.add(latLng.latitude);
            coord.add(latLng.longitude);
            coords.add(coord);
        }

        obj.add("coordinates", coords);
        return obj;
    }
}
