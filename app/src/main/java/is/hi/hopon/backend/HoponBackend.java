package is.hi.hopon.backend;



import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonPoint;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hopon.HoponContext;
import is.hi.hopon.backend.Models.Conversation;
import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.CreateRideDetails;
import is.hi.hopon.backend.Models.DirectionsDetails;
import is.hi.hopon.backend.Models.DirectionsResultList;
import is.hi.hopon.backend.Models.DirectionsResults;
import is.hi.hopon.backend.Models.Geocode;
import is.hi.hopon.backend.Models.GeocodeResult;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;
import is.hi.hopon.backend.Models.Login.UserDetails;
import is.hi.hopon.backend.Models.Message;
import is.hi.hopon.backend.Models.Ride;
import is.hi.hopon.backend.Models.User;
import is.hi.hopon.backend.deserialize.ConversationDeserializer;
import is.hi.hopon.backend.deserialize.DirectionsDeserializer;
import is.hi.hopon.backend.deserialize.GeocodeResultDeserializer;
import is.hi.hopon.backend.deserialize.MessageDeserializer;
import is.hi.hopon.backend.deserialize.PointDeserializer;
import is.hi.hopon.backend.deserialize.RideDeserializer;
import is.hi.hopon.backend.deserialize.UserDeserializer;
import is.hi.hopon.backend.serialize.CreateRideSerializer;
import is.hi.hopon.backend.serialize.DirectionsSerializer;
import is.hi.hopon.backend.serialize.GeocodeSerializer;
import is.hi.hopon.backend.serialize.PointSerializer;

public class HoponBackend {
    private String url;
    public interface HoponBackendResponse<T>
    {
        void onSuccess(T response);
        void onError(Exception error);
    }
    public HoponBackend(String backend_url)
    {
        url = backend_url;
    }

    public class AuthenticationRequired extends Exception{
        public AuthenticationRequired(String message)
        {
            super(message);
        }
    }

    private <Req extends Model, Res extends Model> void hoponBackendRequest(String endpoint, int method, Req data, Res res,  HoponBackendResponse<Res> resultListener, Boolean requireAuthentication)
    {
        if(requireAuthentication)
        {
            if(HoponContext.getInstance().getUser() == null)
            {
                resultListener.onError(new AuthenticationRequired("This endpoint requires authentication"));
            }
        }
        JSONObject payload = new JSONObject();
        if(data != null) {
            try {
                payload = data.toJson();
            } catch (Model.SerializationError ser) {
                resultListener.onError(ser);
                return;
            }
        }
        JsonObjectRequest request = new JsonObjectRequest(
                method, url + endpoint, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            resultListener.onSuccess(res.fromJson(response));
                        }
                        catch(Model.SerializationError ser){
                            resultListener.onError(ser);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultListener.onError(error);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                if(requireAuthentication) {
                    String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                    headers.put("Authorization", bearer);
                    Log.i("HLOG/hoponHeaders", bearer);
                }
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }

    public void login(LoginRequest credentials, HoponBackendResponse<LoginResponse> resultListener)
    {
        hoponBackendRequest("users/authenticate", Request.Method.POST, credentials, new LoginResponse(), resultListener, false);
    }

    public void getUser(int id, HoponBackendResponse<User> listener)
    {
        String path = url + "users/" + id;
        JsonObjectRequest request = new JsonObjectRequest(
                path,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GsonBuilder userBuilder = new GsonBuilder();
                            userBuilder.registerTypeAdapter(User.class, new UserDeserializer());
                            Gson userDeserializer = userBuilder.create();

                            listener.onSuccess(userDeserializer.fromJson(response.toString(), User.class));
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.DEBUG, "user-get-response", "failed");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                headers.put("Authorization", bearer);
                Log.i("HLOG/hoponHeaders", bearer);
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }

    public void getRide(int id, HoponBackendResponse<Ride> listener)
    {
        String path = url + "rides/" + id;
        JsonObjectRequest request = new JsonObjectRequest(
                path,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GsonBuilder userBuilder = new GsonBuilder();
                            userBuilder.registerTypeAdapter(Ride.class, new RideDeserializer());
                            Gson rideDeserializer = userBuilder.create();

                            listener.onSuccess(rideDeserializer.fromJson(response.toString(), Ride.class));
                        } catch (Exception e) {
                            Log.println(Log.DEBUG, "user-serialization", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.DEBUG, "user-get-response", "failed");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                headers.put("Authorization", bearer);
                Log.i("HLOG/hoponHeaders", bearer);
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }

    public void getUsersConversations(int id, HoponBackendResponse<List<Conversation>> listener) {
        String path = url + "messages/user/" + id;

        JsonArrayRequest request = new JsonArrayRequest(
                path,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            GsonBuilder conversationBuilder = new GsonBuilder();
                            conversationBuilder.registerTypeAdapter(Conversation.class, new ConversationDeserializer());
                            Gson conversationDeserializer = conversationBuilder.create();

                            List<Conversation> conversations = new ArrayList<>();
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject conversationJson = response.getJSONObject(i);
                                conversations.add(conversationDeserializer.fromJson(conversationJson.toString(), Conversation.class));
                            }
                            listener.onSuccess(conversations);
                        } catch (Exception e) {
                            Log.println(Log.DEBUG, "user-serialization", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.DEBUG, "user-get-response", "failed");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                headers.put("Authorization", bearer);
                Log.i("HLOG/hoponHeaders", bearer);
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }

    public void getGeocode(Geocode payload, HoponBackendResponse<GeocodeResult> listener) throws JSONException {
        String path = url + "ors/geocode";

        Gson serializer = new GsonBuilder()
                .registerTypeAdapter(Geocode.class, new GeocodeSerializer())
                .create();

        String json = serializer.toJson(payload);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                path,
                new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson deserializer = new GsonBuilder()
                                .registerTypeAdapter(GeocodeResult.class, new GeocodeResultDeserializer())
                                .create();

                        listener.onSuccess(deserializer.fromJson(response.toString(), GeocodeResult.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse data = error.networkResponse;
                        try {
                            String json = new String(data.data, HttpHeaderParser.parseCharset(data.headers));
                            JSONObject obj = new JSONObject(json);
                            Log.println(Log.ERROR, "directions-request-error", obj.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                headers.put("Authorization", bearer);
                Log.i("HLOG/hoponHeaders", bearer);
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }

    public void createRideRequest(CreateRideDetails payload, HoponBackendResponse<Ride> listener) throws JSONException {
        String path = url + "rides/create";
        Gson serializer = new GsonBuilder()
                .registerTypeAdapter(CreateRideDetails.class, new CreateRideSerializer())
                .create();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                path,
                new JSONObject(serializer.toJson(payload)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson rideParser = new GsonBuilder()
                            .registerTypeAdapter(Ride.class, new RideDeserializer())
                            .create();

                        listener.onSuccess(rideParser.fromJson(response.toString(), Ride.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.println(Log.ERROR, "create-ride-request-error", error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                headers.put("Authorization", bearer);
                Log.i("HLOG/hoponHeaders", bearer);
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }

    public void getDirections(DirectionsDetails payload, HoponBackendResponse<DirectionsResultList> listener) throws JSONException {
        String path = url + "ors/directions";
        Gson serializer = new GsonBuilder()
                .registerTypeAdapter(DirectionsDetails.class, new DirectionsSerializer())
                .create();

        JSONArray json = new JSONArray(serializer.toJson(payload));
        Log.println(Log.DEBUG, "directions-request-payload", json.toString());
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.POST,
                path,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson deserializer = new GsonBuilder()
                                .registerTypeAdapter(DirectionsResultList.class, new DirectionsDeserializer())
                                .create();

                        DirectionsResultList results = deserializer.fromJson(response.toString(), DirectionsResultList.class);
                        Log.println(Log.DEBUG, "directions-request", response.toString());
                        listener.onSuccess(results);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse data = error.networkResponse;
                        try {
                            String json = new String(data.data, HttpHeaderParser.parseCharset(data.headers));
                            JSONObject obj = new JSONObject(json);
                            Log.println(Log.ERROR, "directions-request-error", obj.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.println(Log.ERROR, "directions-request-error", error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                String bearer = "Bearer " + HoponContext.getInstance().getUser().getToken();
                headers.put("Authorization", bearer);
                Log.i("HLOG/hoponHeaders", bearer);
                return headers;
            }
        };

        HoponContext.getInstance().getWebQueue().enqueue(request);
    }
}
