package com3001.at00672;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;

import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com3001.at00672.QueryBuilder.generateQuery;

@Service
public class ChatbotService {
    @Autowired
    public ChatRepository chatRepository;
    public Bot bot;
    public Chat chatSession;
    public UserQuery userQuery;

    public ChatbotService() {
        String botName = "wikibot";
        String dir = System.getProperty("user.dir");
        bot = new Bot(botName, dir + "/src/main/resources");
        chatSession = new Chat(bot);
    }

    public Message chatbotRequest(Message request) {
        Message response = new Message(chatSession.multisentenceRespond(request.getContent()), Sender.BOT);
        processRequest(request.getContent(), chatSession.predicates);
        String serverResponse = processResponse(response.getContent());
        response.setContent(serverResponse);
        return response;
    }

    public void processRequest(String request, Predicates predicates) {
        // utilities

        userQuery = new UserQuery();
        userQuery.setTopic(predicates.get("topic"));
        userQuery.setIri(predicates.get("property"));
        userQuery.setFunction(predicates.get("function"));
        userQuery.setValue(WordUtils.capitalize(predicates.get("value")));
        String[] properties = userQuery.getIri().split(":");

        if(properties.length == 2) {
            userQuery.setNamespace(properties[0]);
            userQuery.setProperty(properties[1]);
        }

        System.out.println(String.format("TOPIC: %s, PROPERTY: %s, FUNCTION: %s VALUE: %s", userQuery.getTopic(), userQuery.getProperty(), userQuery.getFunction(), userQuery.getValue()));

        if (request.equals("quit") || request.equals("exit")) {
            System.exit(0);
        }
    }

    public String processResponse(String response) {
        if (userQuery.getFunction().equals("query")) {
            System.out.println(userQuery.toString());
            System.out.println(String.format("TOPIC: %s, PROPERTY: %s, VALUE: %s", userQuery.getTopic(), userQuery.getProperty(), userQuery.getValue()));
            // generate query
            String dbQuery = generateQuery(userQuery);
            userQuery.setQueryString(dbQuery);
            System.out.println(dbQuery);
            String serverResponse = DBPedia.executeQuery(userQuery);
            return serverResponse;
        } else {
            return response;
        }

    }
}
