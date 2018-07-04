package id.go.jabarprov.bappeda.infobappeda.service;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by seagalputra on 7/3/18.
 */

public class NetworkService extends Application {
    private static NetworkService mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private NetworkService(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkService(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return requestQueue;
    }

    public<T> void addToRequest(Request<T> request) {
        requestQueue.add(request);
    }
}
