package id.go.jabarprov.bappeda.infobappeda.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.go.jabarprov.bappeda.infobappeda.R;

public class WelcomeActivity extends AppCompatActivity {
    private Button mBtn_Lanjut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mBtn_Lanjut = (Button)findViewById(R.id.btn_Lanjut);

        mBtn_Lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(iLogin);
                finish();
            }
        });
    }
}
