package id.go.jabarprov.bappeda.infobappeda.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("phone")
    private String phoneNumber;

    public Login() {

    }

    public Login(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
