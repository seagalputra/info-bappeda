package id.go.jabarprov.bappeda.infobappeda.view;

import android.os.Handler;
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
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ProgressBar;

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
import id.go.jabarprov.bappeda.infobappeda.model.Message;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String INTENT_EXTRA = "PHONE_NUMBER";
    // JSON URL DATA
    private static final String URL_DATA = "http://10.13.1.18/info_bappeda/send_msg.php";
    private RequestQueue requestQueue;

    private static final String TAG_NAME = MainActivity.class.getSimpleName();
    private List<Message> messageList = new ArrayList<>();
    private List<Message> filteredMessage = new ArrayList<>();
    private List<Message> tempMessage;

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Toolbar toolbar;

    private Gson gson;

    RecyclerView recyclerView;

    private MessageAdapter adapter;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private ProgressBar progressBar;

    // Session management
    private SessionManagement sessionManagement;
    private String phone;

    private List <Message> showMessage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        progressBar = (ProgressBar)findViewById(R.id.progress);

        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    // fetchData();
                }
            }
        });*/
    }
    /*
    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i<5; i++){

                    showMessage.add(filteredMessage.get(i));
                    mAdapter = new MessageAdapter(showMessage);
                    mRecycleView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
            }
        }, 5000);
    }
    */
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
        StringRequest request = new StringRequest(Request.Method.GET, URL_DATA, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        swipeRefreshLayout.setRefreshing(false);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            messageList = Arrays.asList(gson.fromJson(response, Message[].class));
            tempMessage = new ArrayList<>(messageList);

            for (int i = 0; i < tempMessage.size(); i++) {
                if (tempMessage.get(i).getPhone().equals(phone)) {
                    filteredMessage.add(0, tempMessage.get(i));
                }
            }
            /*

            for (int j = 0; j<5; j++){
                showMessage.add(filteredMessage.get(j));
            }
            mAdapter = new MessageAdapter(showMessage);
            mRecycleView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            */
            mAdapter = new MessageAdapter(filteredMessage);
            mRecycleView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

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

    @Override
    public void onRefresh() {
        mAdapter.notifyDataSetChanged();
        tempMessage.clear();
        filteredMessage.clear();
        fetchPosts();
    }


}
