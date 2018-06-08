package id.go.jabarprov.bappeda.infobappeda.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private String date;
    @SerializedName("phone")
    private String phone;

    public Message() {

    }

    public Message(String message, String date, String phone) {
        this.message = message;
        this.date = date;
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
