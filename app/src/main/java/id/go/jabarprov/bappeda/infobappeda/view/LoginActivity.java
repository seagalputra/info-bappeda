package id.go.jabarprov.bappeda.infobappeda.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.go.jabarprov.bappeda.infobappeda.R;
import id.go.jabarprov.bappeda.infobappeda.model.Login;
import id.go.jabarprov.bappeda.infobappeda.service.NetworkService;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class LoginActivity extends AppCompatActivity {
    private static final String INTENT_EXTRA = "PHONE_NUMBER";
    private static final String TAG_NAME = MainActivity.class.getSimpleName();
    private static final String URL_TOKEN = "http://60.253.116.68/meetingarranger/send_token/";
    private RequestQueue requestQueue;
    private Gson gson;
    private List<Login> loginList = new ArrayList<>();

    private SessionManagement sessionManagement;
    private EditText textPhone;
    private TextInputLayout phoneInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create object from SessionManagement class
        sessionManagement = new SessionManagement(getApplicationContext());

        // Find all view depend it's id
        phoneInputLayout = findViewById(R.id.textphone_text_input_layout);
        textPhone = findViewById(R.id.textPhone);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Set custom date format on the GSON instance to handle date that return by the API
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    public void login(View view) {
        String phone = textPhone.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();

        if (phone.trim().length() > 0) {
            sessionManagement.createLoginSession(phone, token);
            storeUserToken();

            Intent intent = new Intent(getApplicationContext(), VerifyActivity.class);
            intent.putExtra(INTENT_EXTRA, textPhone.getText().toString());
            startActivity(intent);
            finish();

        } else {
            phoneInputLayout.setError(getString(R.string.error_msg_input));
            requestFocus(textPhone);
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void storeUserToken() {
        final String phone = textPhone.getText().toString();
        final String token = FirebaseInstanceId.getInstance().getToken();

        StringRequest request = new StringRequest(Request.Method.POST, URL_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG_NAME, "Success storeUserToken: " + phone + ": " + token);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG_NAME, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_telp", phone);
                params.put("token", token);
                return params;
            }
        };

        NetworkService.getInstance(getApplicationContext()).addToRequest(request);
    }
}
