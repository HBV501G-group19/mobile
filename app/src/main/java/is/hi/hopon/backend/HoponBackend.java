package is.hi.hopon.backend;



import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import is.hi.hopon.HoponContext;
import is.hi.hopon.backend.Models.Core.Model;
import is.hi.hopon.backend.Models.Login.LoginRequest;
import is.hi.hopon.backend.Models.Login.LoginResponse;

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

    public void login(LoginRequest credentials, HoponBackendResponse<LoginResponse> resultListener)
    {
        JSONObject payload;
        try
        {
            payload = credentials.toJson();
        }
        catch (Model.SerializationError ser)
        {
            resultListener.onError(ser);
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url + "users/authenticate",
                payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    resultListener.onSuccess(new LoginResponse().fromJson(response));
                }catch(Model.SerializationError ser)
                {
                    resultListener.onError(ser);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultListener.onError(error);
            }
        });

        HoponContext.getInstance(null).getWebQueue().enqueue(request);
    }


}
