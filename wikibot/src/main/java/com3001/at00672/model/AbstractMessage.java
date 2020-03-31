package com3001.at00672.model;

import javax.persistence.Entity;

// TODO:  probably get rid of these
//@Entity
public class AbstractMessage extends Message {

    private String title;
    private String imageURL;
    private String wikipediaURL;

    public AbstractMessage() {
        super();
        this.messageType = MessageType.ABSTRACT;
    }

    public AbstractMessage(String content, Sender sender) {
        super(content, sender);
        this.messageType = MessageType.ABSTRACT;
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
}
