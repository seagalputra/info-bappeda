package id.go.jabarprov.bappeda.infobappeda.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import id.go.jabarprov.bappeda.infobappeda.view.LoginActivity;
import id.go.jabarprov.bappeda.infobappeda.view.WelcomeActivity;

public class SessionManagement {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private String phoneSession;

    private static SessionManagement mInstance;

    private int PRIVATE_MODE = 0;

    // Shared Preferences name
    private static final String PREF_NAME = "InfoBappedaPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Phone number
    private static final String KEY_PHONE = "phone";

    // Token
    private static final String KEY_TOKEN = "token";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static synchronized SessionManagement getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManagement(context);
        }

        return mInstance;
    }

    public void createLoginSession(String phone, String token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        return user;
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent intent = new Intent(_context, WelcomeActivity.class);

            // Closing all the activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(intent);
        }
    }

    public void userLogout() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(intent);
    }

    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getPhoneNumber() {
        phoneSession = pref.getString(KEY_PHONE, null);
        return phoneSession;
    }

    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
        return true;
    }

    public String getDeviceToken() {
        String deviceToken = pref.getString(KEY_TOKEN, null);
        return deviceToken;
    }
}
