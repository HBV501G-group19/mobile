package is.hi.hopon.backend.serialize;

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
import is.hi.hopon.backend.Models.Geocode;

public class CreateRideSerializer implements JsonSerializer<CreateRideDetails> {
    public JsonElement serialize(CreateRideDetails src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("driver", src.getDriverId());
        obj.addProperty("duration", src.getDuration());


        String departureTime = src.getDepartureTime().format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"));
        obj.addProperty("departureTime", departureTime);

        Gson geoJsonSerializer = new GsonBuilder()
                .registerTypeAdapter(GeoJsonPoint.class, new PointSerializer())
                .registerTypeAdapter(GeoJsonLineString.class, new LineStringSerializer())
                .create();

        JsonElement origin = geoJsonSerializer.toJsonTree(src.getOrigin());
        JsonElement destination = geoJsonSerializer.toJsonTree(src.getDestination());
        JsonElement route = geoJsonSerializer.toJsonTree(src.getRoute());

        obj.add("origin", origin);
        obj.add("destination", destination);
        obj.add("route", route);

        return obj;
    }
}
