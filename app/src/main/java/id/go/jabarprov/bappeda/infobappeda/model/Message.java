package id.go.jabarprov.bappeda.infobappeda.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private String date;
    @SerializedName("phone")
    private String phoneNumber;

    public Message() {

    }

    public Message(String message, String date, String phoneNumber) {
        this.message = message;
        this.date = date;
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
