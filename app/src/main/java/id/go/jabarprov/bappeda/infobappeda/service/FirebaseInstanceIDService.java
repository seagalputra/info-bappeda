package id.go.jabarprov.bappeda.infobappeda.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import id.go.jabarprov.bappeda.infobappeda.session.SessionManagement;

/**
 * Created by seagalputra on 6/29/18.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        saveToken(token);
    }

    private void saveToken(String token) {
        SessionManagement.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
