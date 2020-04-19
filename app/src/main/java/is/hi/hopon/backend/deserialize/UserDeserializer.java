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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.hi.hopon.backend.Models.Ride;
import is.hi.hopon.backend.Models.User;

public class UserDeserializer implements JsonDeserializer<User> {
    Gson rideDeserializer;

    public UserDeserializer() {
        super();
        GsonBuilder ridesBuilder = new GsonBuilder();
        ridesBuilder.registerTypeAdapter(Ride.class, new RideDeserializer());
        rideDeserializer = ridesBuilder.create();
    }

    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject u = json.getAsJsonObject();
        int id = u.getAsJsonPrimitive("id").getAsInt();
        String username = u.getAsJsonPrimitive("username").getAsString();
        JsonArray drivesJson = u.getAsJsonArray("drives");
        List<Ride> drives = getRides(drivesJson);

        JsonArray ridesJson = u.getAsJsonArray("rides");
        List<Ride> rides = getRides(ridesJson);

        return new User(
                id,
                username,
                drives,
                rides
        );
    }

    private List<Ride> getRides(JsonArray ridesJson) {
        List<Ride> rides = new ArrayList<>();
        for(JsonElement r : ridesJson) {
            Ride ride = rideDeserializer.fromJson(r, Ride.class);
            rides.add(ride);
        }
        return rides;
    }
}
