package is.hi.hopon.backend.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<GeoJsonPoint> {
    public JsonElement serialize(GeoJsonPoint src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getType());
        JsonArray coords = new JsonArray();
        coords.add(src.getCoordinates().latitude);
        coords.add(src.getCoordinates().longitude);
        obj.add("coordinates", coords);
        return obj;
    }
}
