package is.hi.hopon;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import is.hi.hopon.backend.HoponBackend;

public class HoponContext {

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
    private static HoponContext instance;
    private static Context ctx;

    private HoponContext(Context context) {
        ctx = context;
        webQueue = new WebQueue();
    }

    public static synchronized HoponContext getInstance(Context context) {
        if (instance == null) {
            instance = new HoponContext(context);
        }
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

}
