package com.vn.tcshop.aitechc.Models;

public class ChatImgBot {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";
    public static String SENT_BY_BOTT = "bott";

    String message;
    String sentBy;

    public ChatImgBot(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
