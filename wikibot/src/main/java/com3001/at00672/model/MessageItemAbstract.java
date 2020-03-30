package com3001.at00672.model;

public class MessageItemAbstract extends MessageItem {

    private String imageURL;
    private String name;
    private String wikipediaURL;

    public MessageItemAbstract() {
        super();
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWikipediaURL() {
        return wikipediaURL;
    }

    public void setWikipediaURL(String wikipediaURL) {
        this.wikipediaURL = wikipediaURL;
    }
}
