package com.itcom202.weroom.chat;

public class Message {

    private String content;
    private String senderID;
    private String timeStamp;

    public Message (String content, String sender) {

        this.content = content;
        this.senderID = sender;
        long tsLong = System.currentTimeMillis()/1000;
        timeStamp = Long.toString(tsLong);
    }

    public String getContent() {
        return content;
    }
    public String getSenderID() {
        return senderID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}