package com3001.at00672.service;

import java.util.ArrayList;
import java.util.Arrays;

import com3001.at00672.model.*;

import org.alicebot.ab.*;

import static com3001.at00672.service.QueryBuilder.generateQuery;

//@Service
public class ChatbotService {
    public MessageRepository messageRepository;
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
        processResponse(userQuery, response);
        return response;
    }

    public void processResponse(UserQuery userQuery, Message botResponse) {
        if (queryKeywords.contains(userQuery.get("function"))) {
            String serverResponse = "";
            System.out.println(userQuery.toString());
            // generate query
            String dbQuery = generateQuery(userQuery);
            userQuery.setQueryString(dbQuery);
            System.out.println(dbQuery);

            if (userQuery.getQueryString() != "")
                botResponse.setMessageItems(DBPedia.executeQuery(userQuery));
            else {
                botResponse.addMessageItem(new MessageItem("Sorry, I don't know"));
            }
        } else {
            botResponse.addMessageItem(new MessageItem(botResponse.getContent()));
        }

    }
}
