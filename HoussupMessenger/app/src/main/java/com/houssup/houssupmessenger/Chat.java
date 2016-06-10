package com.houssup.houssupmessenger;

/**
 * Created by SOMEX on 04-06-2016.
 */
public class Chat {
    private String message;
    private String sender;
    private String time;
    private String id;
    private String hasAttachment;
    private String attachmentLink;


    public Chat() {
    }


    public Chat(String message, String id, String sender, String time,String hasAttachment,String attachmentLink) {
        this.message = message;
        this.id = id;
        this.sender=sender;
        this.time= time;
        this.hasAttachment=hasAttachment;
        this.attachmentLink= attachmentLink;
    }


    public String getMessage() {
        return message;
    }
    public String getSender() {
        return sender;
    }
    public String getTime() {
        return time;
    }
    public String getHasAttachment() {
        return hasAttachment;
    }
    public String getAttachmentLink() {
        return attachmentLink;
    }
    public String getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setTIme(String time) {
        this.time = time;
    }
    public void setHasAttachment(String hasAttachment) {
        this.hasAttachment = hasAttachment;
    }
    public void setAttachmentLink(String attachmentLink) {
        this.attachmentLink = attachmentLink;
    }

    public void setId(String id) {
        this.id = id;
    }
}