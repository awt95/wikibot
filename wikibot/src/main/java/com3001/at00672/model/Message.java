package com3001.at00672.model;

import javax.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private String content;
    private Sender sender;

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
}
