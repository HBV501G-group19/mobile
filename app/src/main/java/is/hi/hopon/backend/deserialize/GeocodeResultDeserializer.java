package is.hi.hopon.backend.deserialize;

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
import com.google.maps.android.data.geojson.GeoJsonPoint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import is.hi.hopon.backend.Models.GeocodeResult;

public class GeocodeResultDeserializer implements JsonDeserializer<GeocodeResult> {
    public GeocodeResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject p = json.getAsJsonObject();
        JsonArray featuresJson = p.getAsJsonArray("features");
        List<GeoJsonFeature> features = new ArrayList<>();

        Gson pointsDeserializer = new GsonBuilder()
                .registerTypeAdapter(GeoJsonPoint.class, new PointDeserializer())
                .create();

        Gson mapParser = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();

        for (JsonElement featureJson : featuresJson) {
            JsonObject featureObj = featureJson.getAsJsonObject();
            JsonObject geometryJson = featureObj.getAsJsonObject("geometry");
            GeoJsonPoint point = pointsDeserializer.fromJson(geometryJson, GeoJsonPoint.class);
            JsonObject propertiesJson = featureObj.getAsJsonObject("properties");

            HashMap<String, Object> propertiesTemp = mapParser.fromJson(propertiesJson, type);
            HashMap<String, String> properties = new HashMap<>();
            for(String key : properties.keySet()) {
                String property = String.valueOf(propertiesTemp.get(key));
                properties.put(key, property);
            }

            GeoJsonFeature feature = new GeoJsonFeature(point, null, properties, null);

            features.add(feature);
        }

        return new GeocodeResult(features);
    }
}
