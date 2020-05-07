package com3001.at00672.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Message {

    @Id
    @GeneratedValue
    protected Long id;
    @Lob
    protected String content;
    protected Sender sender;
    protected MessageType messageType = MessageType.TEXT;

    private String session;

    private String title;
    private String imageURL;
    private String wikipediaURL;

    @OneToMany(cascade = {CascadeType.ALL})
    protected List<MessageItem> messageItems = new ArrayList<>();

    public Message() {}

    public Message(String content, Sender sender) {
        this.content = content;
        this.sender = sender;
    }

    public Message(String content, Sender sender, MessageType messageType) {
        this.content = content;
        this.sender = sender;
        this.messageType = messageType;
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

    public void addMessageItem(MessageItem messageItem) {
        messageItems.add(messageItem);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getWikipediaURL() {
        return wikipediaURL;
    }

    public void setWikipediaURL(String wikipediaURL) {
        this.wikipediaURL = wikipediaURL;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
