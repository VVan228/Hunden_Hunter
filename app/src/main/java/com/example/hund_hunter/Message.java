package com.example.hund_hunter;

import java.util.Date;

public class Message {
    public String UserName;
    public String text_message;
    private long message_time;

    public Message() {}
    public Message(String userName, String text_message) {
        this.UserName = userName;
        this.text_message = text_message;

        this.message_time = new Date().getTime();
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getText_message() {
        return text_message;
    }

    public void setText_message(String text_message) {
        this.text_message = text_message;
    }

    public long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }
}
