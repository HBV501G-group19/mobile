package is.hi.hopon.backend;



import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import is.hi.hopon.HoponContext;
import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;
import is.hi.hopon.backend.Models.Login.UserDetails;

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


}
