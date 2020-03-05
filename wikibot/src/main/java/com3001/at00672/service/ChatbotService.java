package com3001.at00672.service;

import java.util.ArrayList;
import java.util.Arrays;

import com3001.at00672.model.ChatRepository;
import com3001.at00672.model.UserQuery;
import com3001.at00672.model.Message;
import com3001.at00672.model.Sender;
import org.apache.commons.text.WordUtils;

import org.alicebot.ab.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com3001.at00672.service.QueryBuilder.generateQuery;

//@Service
public class ChatbotService {
    public ChatRepository chatRepository;
    public Bot bot;
    public Chat chatSession;
    public ArrayList<String> queryKeywords = new ArrayList<>(Arrays.asList("query", "list", "list_conditional"));

    public ChatbotService() {
        String botName = "wikibot";
        String dir = System.getProperty("user.dir");
        bot = new Bot(botName, dir + "/src/main/resources");
        chatSession = new Chat(bot);
    }

    public Message chatbotRequest(Message request) {
        Message response = new Message(chatSession.multisentenceRespond(request.getContent()), Sender.BOT);
        UserQuery userQuery = new UserQuery(chatSession.predicates);
        String serverResponse = processResponse(userQuery, response.getContent());
        response.setContent(serverResponse);
        return response;
    }

    public String processResponse(UserQuery userQuery, String response) {
        if (queryKeywords.contains(userQuery.get("function"))) {
            String serverResponse = "";
            System.out.println(userQuery.toString());
            // generate query
            String dbQuery = generateQuery(userQuery);
            userQuery.setQueryString(dbQuery);
            System.out.println(dbQuery);

            if (userQuery.getQueryString() != "")
                serverResponse = DBPedia.executeQuery(userQuery);
            else
                serverResponse = "Sorry, I don't know";
            return serverResponse;
        } else {
            return response;
        }

    }
}
