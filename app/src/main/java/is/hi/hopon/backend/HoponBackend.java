package is.hi.hopon.backend;



import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.hopon.HoponContext;
import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;
import is.hi.hopon.backend.Models.Login.UserDetails;
import is.hi.hopon.backend.Models.Ride;

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

    public void userDetails(String id, HoponBackendResponse<UserDetails> resultListener)
    {
        hoponBackendRequest("users/"+id, Request.Method.GET, null, new UserDetails(), resultListener, true);
    }

    private void jsonArrayRequest(String endpoint, int method, JSONArray payload, HoponBackendResponse<JSONArray> results)
    {
        if(HoponContext.getInstance().getUser() == null)
            {
                results.onError(new AuthenticationRequired("This endpoint requires authentication"));
            }
            JsonArrayRequest request = new JsonArrayRequest(
                method, url + endpoint, payload,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        results.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof AuthFailureError){
                    HoponContext.getInstance().logOut();
                }
                results.onError(error);
            }
        }
        ){
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

    public void rides(HoponBackendResponse<List<Ride>> result)
    {
        jsonArrayRequest("rides", Request.Method.GET, null, new HoponBackendResponse<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {try {
                Integer myId = Integer.parseInt(HoponContext.getInstance().getUser().getId());
                ArrayList<Ride> rideList = new ArrayList<Ride>();
                for(int i=0; i<response.length();i++){
                    JSONObject entry = response.getJSONObject(i);
                    JSONArray jsonPassengers = entry.getJSONArray("passengers");
                    ArrayList<Integer> passengers = new ArrayList<Integer>();
                    for(int p=0; p<jsonPassengers.length();p++){
                        passengers.add(jsonPassengers.getInt(p));
                    }

                    Integer driver = entry.getInt("driver");
                    if(!(entry.getInt("driver") == myId || passengers.contains(myId))) continue;
                    JSONArray jsonOrig = entry.getJSONObject("origin").getJSONArray("coordinates");
                    JSONArray jsonDest = entry.getJSONObject("destination").getJSONArray("coordinates");
                    rideList.add(new Ride(
                       entry.getInt("driver"),
                       entry.getString("departureTime"),
                       entry.getInt("duration"),
                       new LatLng(jsonOrig.getDouble(0), jsonOrig.getDouble(1)),
                       new LatLng(jsonDest.getDouble(0), jsonDest.getDouble(1)),
                       new ArrayList<LatLng>(),
                       entry.getInt("freeSeats"),
                       passengers
                    ));
                }
                result.onSuccess(rideList);
            } catch (JSONException e) {
                result.onError(e);
            }
            }

            @Override
            public void onError(Exception error) {
                result.onError(error);
            }
        });
    }

}
