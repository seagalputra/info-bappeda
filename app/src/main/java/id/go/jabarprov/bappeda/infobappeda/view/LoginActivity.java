package id.go.jabarprov.bappeda.infobappeda.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    private static final String URL_LOGIN = "http://10.13.1.18/info_bappeda/login.php";
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

        phoneInputLayout = findViewById(R.id.textphone_text_input_layout);
        textPhone = findViewById(R.id.textPhone);
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

            if (isPhone) {
                sessionManagement.createLoginSession(phone);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(INTENT_EXTRA, textPhone.getText().toString());
                startActivity(intent);
                finish();
            } else {
                phoneInputLayout.setError(getString(R.string.error_msg_phone_input));
                requestFocus(textPhone);
            }
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
}
