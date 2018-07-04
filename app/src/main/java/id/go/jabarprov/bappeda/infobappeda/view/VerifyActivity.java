package id.go.jabarprov.bappeda.infobappeda.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.go.jabarprov.bappeda.infobappeda.R;
import id.go.jabarprov.bappeda.infobappeda.service.NetworkService;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class VerifyActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA = "PHONE_NUMBER";
    private static final String TAG_NAME = VerifyActivity.class.getSimpleName();
    private EditText otpEditText;
    private TextInputLayout otpInputLayout;

    private SessionManagement sessionManagement;

    private String otpCodeObj;
    private Intent intent;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        sessionManagement = new SessionManagement(getApplicationContext());

        otpEditText = findViewById(R.id.input_otp);
        otpInputLayout = findViewById(R.id.input_layout_otp);

        intent = getIntent();
        phoneNumber = intent.getStringExtra(INTENT_EXTRA);

        getOTP(phoneNumber);
    }

    private void getOTP(String phoneNumber) {
        String URL_OTP = "http://10.13.1.18/mysms/ver_code.php?number=" + phoneNumber + "&cid=infobappeda";

        StringRequest request = new StringRequest(Request.Method.GET, URL_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                otpCodeObj = response.toString();
                Log.d(TAG_NAME, " OTP Code : " + otpCodeObj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG_NAME, "onErrorResponse: " + error.toString());
            }
        });

        NetworkService.getInstance(getApplicationContext()).addToRequest(request);
    }

    public void verify(View view) {
        String otpCode = otpEditText.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();

        if (otpCode.trim().length() > 0) {
            if (otpCodeObj.equals(otpCode)) {
                sessionManagement.createLoginSession(phoneNumber, token);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                otpInputLayout.setError("Kode OTP salah, silahkan cek kode anda");
                requestFocus(otpEditText);
            }
        } else {
            otpInputLayout.setError("Masukkan kode OTP anda");
            requestFocus(otpEditText);
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
