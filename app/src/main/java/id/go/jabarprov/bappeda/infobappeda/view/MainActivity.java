package id.go.jabarprov.bappeda.infobappeda.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.go.jabarprov.bappeda.infobappeda.R;
import id.go.jabarprov.bappeda.infobappeda.adapter.MessageAdapter;
import id.go.jabarprov.bappeda.infobappeda.model.Message;
import id.go.jabarprov.bappeda.infobappeda.service.NetworkService;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String INTENT_EXTRA = "PHONE_NUMBER";

    // JSON URL DATA
    private static final String URL_DATA = "http://60.253.116.68/info_bappeda/send_msg.php";

    private static final String TAG_NAME = MainActivity.class.getSimpleName();
    private List<Message> messageList = new ArrayList<>();
    private List<Message> filteredMessage = new ArrayList<>();
    private List<Message> tempMessage;

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Toolbar toolbar;

    private Gson gson;

    // Session management
    private SessionManagement sessionManagement;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar as AppBar
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Check user login
        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        phone = sessionManagement.getPhoneNumber();

        // Find RecyclerView in main_activity layout and define it's properties
        mRecycleView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Set custom date format on the GSON instance to handle date that return by the API
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        // Call method fetchPosts
        fetchPosts();

        // Firebase
        FirebaseMessaging.getInstance().subscribeToTopic("info_bappeda");

        // Create properties for RecyclerView
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:
                sessionManagement.userLogout();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                messageList = Arrays.asList(gson.fromJson(response, Message[].class));

                tempMessage = new ArrayList<>(messageList);

                for (int i = 0; i < tempMessage.size(); i++) {
                    if (tempMessage.get(i).getPhone().equals(phone)) {
                        filteredMessage.add(0, tempMessage.get(i));
                    }
                }

                mAdapter = new MessageAdapter(filteredMessage);
                mRecycleView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                Log.i(TAG_NAME, messageList.size() + " messages loaded.");
                for (Message message : messageList) {
                    Log.i(TAG_NAME, message.getDate() + ": " + message.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG_NAME, "onErrorResponse: " + error.toString());
            }
        });
        NetworkService.getInstance(getApplicationContext()).addToRequest(request);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mAdapter.notifyDataSetChanged();
        tempMessage.clear();
        filteredMessage.clear();
        fetchPosts();
    }
}