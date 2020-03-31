package com3001.at00672.model;

public class ListMessage extends Message {
    public ListMessage() {
        super();
        this.setMessageType(MessageType.LIST);
    }

    public ListMessage(String content, Sender sender) {
        super(content, sender);
        this.setMessageType(MessageType.LIST);
    }
}
