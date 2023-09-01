package com.example.chatapp.Model;

public class MessageModel {
    private  String msgId;
    private  String senderId;
    private  String message;
    private long timestamp;

    private  String mediaType;
    private  String mediaURL;
    private  String mediaName;

    public MessageModel(String msgId, String senderId, String message,long timestamp) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }
    public MessageModel(String msgId, String senderId, String message,long timestamp,String mediaType,String mediaURL,String mediaName ) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
        this.mediaType = mediaType;
        this.mediaURL = mediaURL;
        this.mediaName = mediaName;
    }

    public MessageModel() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "msgId='" + msgId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", mediaType='" + mediaType + '\'' +
                ", mediaURL='" + mediaURL + '\'' +
                ", mediaName='" + mediaName + '\'' +
                '}';
    }
}
