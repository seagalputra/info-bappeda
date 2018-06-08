package id.go.jabarprov.bappeda.infobappeda.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import id.go.jabarprov.bappeda.infobappeda.model.Login;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class LoginActivity extends AppCompatActivity {
    private static final String INTENT_EXTRA = "PHONE_NUMBER";
    private static final String TAG_NAME = MainActivity.class.getSimpleName();
    private static final String URL_LOGIN = "https://api.jsonbin.io/b/5b18b16dc2e3344ccd96cee1";
    private RequestQueue requestQueue;
    private Gson gson;
    private List<Login> loginList = new ArrayList<>();

    private SessionManagement sessionManagement;
    private EditText textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create object from SessionManagement class
        sessionManagement = new SessionManagement(getApplicationContext());

        textPhone = (EditText) findViewById(R.id.textPhone);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Set custom date format on the GSON instance to handle date that return by the API
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchData();
    }

    private void fetchData() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_LOGIN, onDataLoaded, onDataError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onDataLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            loginList = Arrays.asList(gson.fromJson(response, Login[].class));
            Log.i(TAG_NAME, loginList.size() + " data loaded");

            for (Login login : loginList) {
                Log.i(TAG_NAME, login.getPhoneNumber());
            }
        }
    };

    private final Response.ErrorListener onDataError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG_NAME, error.toString());
        }
    };

    public void login(View view) {
        String phone = textPhone.getText().toString();
        Boolean isPhone = false;

        for (Login login : loginList) {
            if (login.getPhoneNumber().equals(phone)) {
                isPhone = true;
            }
        }

        if (phone.trim().length() > 0) {
            if (isPhone == true) {
                sessionManagement.createLoginSession(phone);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(INTENT_EXTRA, textPhone.getText().toString());
                startActivity(intent);
                finish();
            } else {
                //TODO Create alert dialog if phone number is incorect
                Toast.makeText(this, "Phone number is incorrect", Toast.LENGTH_LONG).show();
            }
        } else {
            //TODO Create alert dialog if phone number is blank
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_LONG).show();
        }
    }
}
