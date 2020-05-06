package com3001.at00672.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import com3001.at00672.model.*;

import org.alicebot.ab.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static com3001.at00672.service.QueryBuilder.generateQuery;

//@Service
public class ChatbotService {
    public Bot bot;
    public Chat chatSession;
    public ArrayList<String> queryKeywords = new ArrayList<>(Arrays.asList("query", "abstract", "list", "list_conditional", "calculateAge"));

    public ChatbotService() throws IOException {
        String botName = "wikibot";
        File botFolder = new ClassPathResource("").getFile();
        bot = new Bot(botName, botFolder.getPath(), "chat");
        chatSession = new Chat(bot);
    }

    public Message chatbotRequest(Message request) {
        String responseString = chatSession.multisentenceRespond(request.getContent());
        UserQuery userQuery = new UserQuery(chatSession.predicates);
        Message response;
        if (chatSession.predicates.get("function").equalsIgnoreCase("abstract")) {
            response = new Message(responseString, Sender.BOT, MessageType.ABSTRACT);
        } else if (chatSession.predicates.get("function").contains("list")) {
            response = new Message(responseString, Sender.BOT, MessageType.LIST);
        } else {
            response = new Message(responseString, Sender.BOT, MessageType.TEXT);
        }
        processResponse(userQuery, response);
        return response;
    }

    public void processResponse(UserQuery userQuery, Message botResponse) {
        System.out.println(userQuery.toString());
        if (queryKeywords.contains(userQuery.get("function"))) {
            // generate query
            String dbQuery = QueryBuilder.generateQuery(userQuery);
            userQuery.setQueryString(dbQuery);
            System.out.println(dbQuery);

            if (userQuery.getQueryString() != "") {
                DBPedia.executeQuery(userQuery, botResponse);
            } else {
                botResponse.setContent("Sorry, I don't know");
            }
        } else {
           botResponse.addMessageItem(new MessageItem(botResponse.getContent()));
        }

    }
}
