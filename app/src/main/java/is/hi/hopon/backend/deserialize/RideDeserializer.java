package is.hi.hopon.backend.deserialize;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hopon.backend.Models.Ride;

public class RideDeserializer implements JsonDeserializer<Ride> {
    Gson pointDeserializer;
    Gson lineStringDeserializer;

    public RideDeserializer() {
        super();
        GsonBuilder pointsBuilder = new GsonBuilder();
        pointsBuilder.registerTypeAdapter(GeoJsonPoint.class, new PointDeserializer());
        pointDeserializer = pointsBuilder.create();

        GsonBuilder lineStringBuilder = new GsonBuilder();
        lineStringBuilder.registerTypeAdapter(GeoJsonLineString.class, new LineStringDeserializer());
        lineStringDeserializer = lineStringBuilder.create();
    }

    public Ride deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject r = json.getAsJsonObject();
        int driver = r.getAsJsonPrimitive("driver").getAsInt();
        String departureString = r.getAsJsonPrimitive("departureTime").getAsString().replace("T", " ").replace(".000+0000", "");
        LocalDateTime departure = LocalDateTime.parse(departureString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        long duration = r.getAsJsonPrimitive("duration").getAsLong();
        short freeSeats = r.getAsJsonPrimitive("freeSeats").getAsShort();
        short totalSeats = r.getAsJsonPrimitive("totalSeats").getAsShort();

        List<Integer> passengers = new ArrayList<>();
        JsonArray passengerJson = r.getAsJsonArray("passengers");
        for(JsonElement j : passengerJson) {
            int id = j.getAsInt();
            passengers.add(id);
        }

        JsonObject originJson = r.getAsJsonObject("origin");
        JsonObject destinationJson = r.getAsJsonObject("destination");
        JsonObject routeJson = r.getAsJsonObject("route");

        GeoJsonPoint origin = pointDeserializer.fromJson(originJson, GeoJsonPoint.class);
        GeoJsonPoint destination = pointDeserializer.fromJson(destinationJson, GeoJsonPoint.class);
        GeoJsonLineString route = lineStringDeserializer.fromJson(routeJson, GeoJsonLineString.class);

        JsonObject propertiesJson = r.getAsJsonObject("properties");

        Gson mapParser = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> properties = mapParser.fromJson(propertiesJson, type);

        return new Ride(
                driver,
                departure,
                duration,
                origin,
                destination,
                route,
                freeSeats,
                totalSeats,
                passengers,
                properties
        );
    }
}
