package com3001.at00672;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    public static List<Message> messages;

    public Conversation() {
        messages = new ArrayList<>();
        messages.add(new Message("test"));
    }

    public static List<Message> getMessages() {
        return messages;
    }

    public static void setMessages(List<Message> messages) {
        Conversation.messages = messages;
    }

    public static void addMessage(Message message) {
        messages.add(message);
    }
}
