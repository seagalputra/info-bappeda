package id.go.jabarprov.bappeda.infobappeda.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.go.jabarprov.bappeda.infobappeda.R;
import id.go.jabarprov.bappeda.infobappeda.adapter.MessageAdapter;
import id.go.jabarprov.bappeda.infobappeda.model.Login;
import id.go.jabarprov.bappeda.infobappeda.model.Message;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class MainActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA = "PHONE_NUMBER";
    // JSON URL DATA
    private static final String URL_DATA = "https://api.jsonbin.io/b/5b177b28c83f6d4cc734aaff/2";
    private RequestQueue requestQueue;

    private static final String TAG_NAME = MainActivity.class.getSimpleName();
    private List<Message> messageList = new ArrayList<>();
    private List<Message> filteredMessage = new ArrayList<>();

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressDialog pDialog;

    private Gson gson;

    // Session management
    private SessionManagement sessionManagement;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check user login
        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        phone = sessionManagement.getPhoneNumber();

        // Find RecyclerView in main_activity layout and define it's properties
        mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);

        // Make request queue from volley
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Set custom date format on the GSON instance to handle date that return by the API
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchPosts();

        // Create properties for RecyclerView
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_DATA, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            messageList = Arrays.asList(gson.fromJson(response, Message[].class));

            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i).getPhone().equals(phone)) {
                    filteredMessage.add(messageList.get(i));
                }
            }

            mAdapter = new MessageAdapter(filteredMessage);
            mRecycleView.setAdapter(mAdapter);

            Log.i(TAG_NAME, messageList.size() + " messages loaded.");
            for (Message message : messageList) {
                Log.i(TAG_NAME, message.getDate() + ": " + message.getMessage());
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG_NAME, error.toString());
        }
    };

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
