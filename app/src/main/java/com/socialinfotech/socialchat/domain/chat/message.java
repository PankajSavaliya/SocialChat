package com.socialinfotech.socialchat.domain.chat;

/**
 * Created by pankaj on 05/04/16.
 */
public class message {
    String sender;
    String receiver;
    String message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
