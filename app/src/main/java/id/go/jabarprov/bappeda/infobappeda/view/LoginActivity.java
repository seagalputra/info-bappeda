package id.go.jabarprov.bappeda.infobappeda.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.go.jabarprov.bappeda.infobappeda.R;
import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

public class LoginActivity extends AppCompatActivity {
    private static final String URL_LOGIN = "https://api.jsonbin.io/b/5b18b16dc2e3344ccd96cee1";
    private Button btnLogin;
    private SessionManagement sessionManagement;
    private EditText textPhone;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManagement = new SessionManagement(getApplicationContext());

        textPhone = (EditText) findViewById(R.id.textPhone);
        Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManagement.isLoggedIn(), Toast.LENGTH_LONG).show();
    }

    public void login(View view) {
        String phone = textPhone.getText().toString();

        if (phone.trim().length() > 0) {
            if (phone.equals("085733083245")) {
                sessionManagement.createLoginSession("085733083245");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
