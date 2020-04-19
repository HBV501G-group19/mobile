package is.hi.hopon.backend.serialize;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import is.hi.hopon.backend.Models.CreateRideDetails;
import is.hi.hopon.backend.Models.DirectionsDetails;
import is.hi.hopon.backend.Models.Geocode;

public class DirectionsSerializer implements JsonSerializer<DirectionsDetails> {
    public JsonElement serialize(DirectionsDetails src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        String profile = src.getProperties().get("profile");

        JsonObject properties = new JsonObject();
        properties.addProperty("profile", profile);

        Gson geoJsonSerializer = new GsonBuilder()
                .registerTypeAdapter(GeoJsonPoint.class, new PointSerializer())
                .registerTypeAdapter(GeoJsonLineString.class, new LineStringSerializer())
                .create();

        JsonElement origin = geoJsonSerializer.toJsonTree(src.getOrigin());
        JsonElement destination = geoJsonSerializer.toJsonTree(src.getDestination());

        JsonElement originFlipped = flipCoords(origin);
        JsonElement destinationFlipped = flipCoords(destination);

        obj.add("origin", originFlipped);
        obj.add("destination", destinationFlipped);
        obj.add("properties", properties);

        Log.println(Log.DEBUG, "directions-serialized", obj.toString());

        JsonArray array = new JsonArray();
        array.add(obj);
        return array;
    }

    private JsonObject flipCoords(JsonElement point) {
        JsonArray coords = point.getAsJsonObject().getAsJsonArray("coordinates");

        JsonElement temp = coords.get(0);
        coords.set(0, coords.get(1));
        coords.set(1, temp);

        JsonObject flipped = new JsonObject();
        flipped.addProperty("type", "Point");
        flipped.add("coordinates", coords);

        return flipped;
    }
}
