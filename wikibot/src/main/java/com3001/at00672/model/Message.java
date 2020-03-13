package com3001.at00672.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

//@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private String content;
    private Sender sender;
    private String imageUrl;
    private MessageType messageType;

    @OneToMany
    private List<MessageItem> messageItems;

    public Message() {}

    public Message(String content, Sender sender) {
        this.content = content;
        this.sender = sender;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public List<MessageItem> getMessageItems() {
        return messageItems;
    }

    public void setMessageItems(List<MessageItem> messageItems) {
        this.messageItems = messageItems;
    }
}
