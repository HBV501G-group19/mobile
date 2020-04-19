package is.hi.hopon.backend.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;

import is.hi.hopon.backend.Models.Geocode;

public class GeocodeSerializer implements JsonSerializer<Geocode> {
    public JsonElement serialize(Geocode src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("geocode", src.getGeocode());

        Gson pointSerializer = new GsonBuilder()
                .registerTypeAdapter(GeoJsonPoint.class, new PointSerializer())
                .create();

        JsonElement focus = pointSerializer.toJsonTree(src.getFocus());
        obj.add("focus", focus);

        return obj;
    }
}
