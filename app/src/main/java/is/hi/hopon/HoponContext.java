package is.hi.hopon;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import is.hi.hopon.backend.HoponBackend;
import is.hi.hopon.backend.Models.Login.LoginResponse;

public class HoponContext {

    public class LoggedUser
    {
        private String username;
        private String  id; // TODO: Change to int when fixed on backend
        private String token;

        public LoggedUser(String id, String username, String token)
        {
            this.id = id;
            this.username = username;
            this.token = token;
        }

        public String getUsername(){ return username; }
        public String getId() { return id; }
        public String getToken() { return token; }
    }

    public class WebQueue{
        private RequestQueue requestQueue;
        private HoponBackend backend;
        private WebQueue()
        {
            requestQueue = get();
            backend = new HoponBackend("https://ancient-wave-00930.herokuapp.com/");
        }
        public RequestQueue get() {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            }
            return requestQueue;
        }
        public <T> void enqueue(com.android.volley.Request req) {
            get().add(req);
        }

    }

    private WebQueue webQueue;
    private SharedPreferences storage;
    private static HoponContext instance;
    private static Context ctx;
    private LoggedUser user;

    private static final String KEY_ID = "USER_ID";
    private static final String KEY_UNAME = "USER_NAME";
    private static final String KEY_TOKEN = "USER_TOKEN";


    private HoponContext(Context context) {
        ctx = context;
        webQueue = new WebQueue();
        storage = context.getSharedPreferences("UDATA", Context.MODE_PRIVATE);
     }

    public static synchronized HoponContext getInstance(Context context) {
        if (instance == null) {
            instance = new HoponContext(context);
        }
        return instance;
    }

    public static synchronized HoponContext getInstance()
    {
        return instance;
    }

    public WebQueue getWebQueue()
    {
        return webQueue;
    }

    public HoponBackend getBackend()
    {
        return webQueue.backend;
    }

    public LoggedUser logIn(LoginResponse loginResponse)
    {
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(KEY_ID, loginResponse.id);
        editor.putString(KEY_TOKEN, loginResponse.token);
        editor.putString(KEY_UNAME, loginResponse.username);
        editor.apply();
        user = new LoggedUser(loginResponse.id, loginResponse.username, loginResponse.token);
        Log.i("HLOG/logIn", "User logged");
        return user;
    }

    public LoggedUser getUser()
    {
        if(user != null) {
            Log.i("HLOG/getUser", "User already logged");
            return user;
        }
        String token = storage.getString(KEY_TOKEN, "");
        if(!token.isEmpty())
        {
            String id = storage.getString(KEY_ID, "");
            String username = storage.getString(KEY_UNAME, "");
            user = new LoggedUser(id, username, token);
            Log.i("HLOG/getUser", "Returning user from storage");
            return user;
        }
        Log.i("HLOG/getUser", "User not logged");
        return null;
    }

    public void logOut()
    {
        Log.i("HLOG/logOut", "Logged out");
        user = null;
        SharedPreferences.Editor editor = storage.edit();
        editor.remove(KEY_ID);
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_UNAME);
        editor.apply();
    }

}
