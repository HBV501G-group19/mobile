package is.hi.hopon.backend.deserialize;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import is.hi.hopon.backend.Models.DirectionsResultList;
import is.hi.hopon.backend.Models.DirectionsResults;
import is.hi.hopon.backend.Models.GeocodeResult;

public class DirectionsDeserializer implements JsonDeserializer<DirectionsResultList> {
    public DirectionsResultList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray a = json.getAsJsonArray();
         Gson gson = new GsonBuilder()
                 .registerTypeAdapter(GeoJsonPoint.class, new PointDeserializer())
                 .registerTypeAdapter(GeoJsonLineString.class, new LineStringDeserializer())
                 .create();

        Gson mapParser = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {}.getType();

        List<DirectionsResults> results = new ArrayList<>();
        for (JsonElement el : a) {
            List<GeoJsonFeature> points = new ArrayList<>();
            GeoJsonFeature path = null;

            JsonObject obj = el.getAsJsonObject();
            JsonArray featuresJson = obj.getAsJsonArray("features");
            for (JsonElement f : featuresJson) {
                JsonObject featureJson = f.getAsJsonObject();
                JsonObject geometry = featureJson.getAsJsonObject("geometry");
                String geometryType = geometry.getAsJsonPrimitive("type").getAsString();
                JsonObject propertiesJson = featureJson.getAsJsonObject("properties");

                HashMap<String, Object> propertiesTemp = mapParser.fromJson(propertiesJson, type);
                HashMap<String, String> properties = new HashMap<>();
                for(String key : properties.keySet()) {
                    String property = String.valueOf(propertiesTemp.get(key));
                    Log.println(Log.DEBUG, "making-properties", property);
                    properties.put(key, property);
                }
                if (geometryType.toLowerCase().equals("point")) {
                    GeoJsonPoint point = gson.fromJson(geometry, GeoJsonPoint.class);
                    points.add(new GeoJsonFeature(point, "", properties, null));
                } else {
                    Log.println(Log.DEBUG, "linestring-tail", geometryType);
                    GeoJsonLineString lineString = gson.fromJson(geometry, GeoJsonLineString.class);
                    path = new GeoJsonFeature(lineString, "", properties, null);
                }
            }

            DirectionsResults result = new DirectionsResults(path, points);
            results.add(result);
        }
        return new DirectionsResultList(results);
    }
}
