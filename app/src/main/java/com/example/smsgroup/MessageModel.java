package com.example.smsgroup;

public class MessageModel {
    private String name, message, uid;

    public MessageModel(String name, String message, String uid) {
        this.name = name;
        this.message = message;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }
}
