package id.go.jabarprov.bappeda.infobappeda.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private String date;

    public Message() {

    }

    public Message(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
