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
    private ChatRepository chatRepository;
    private Bot bot;
    private Chat chatSession;
    private UserQuery userQuery;

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
        String topic = predicates.get("topic");
        String iri = predicates.get("property");
        String value = predicates.get("value");
        value = WordUtils.capitalize(value);
        String[] properties = iri.split(":");
        String namespace = null;
        String property = null;
        if(properties.length == 2) {
            namespace = properties[0];
            property = properties[1];
        } else {
            property = predicates.get("property");
        }
        userQuery = new UserQuery(topic, iri, value, namespace, property);
    }

    public String processResponse(String response) {
        System.out.println(userQuery.toString());
        System.out.println(String.format("TOPIC: %s, PROPERTY: %s, VALUE: %s", userQuery.getTopic(), userQuery.getProperty(), userQuery.getValue()));
        if (userQuery.getTopic().equals("conversation")) {
            return response;
        } else {
            String dbQuery = generateQuery(userQuery);
            userQuery.setQueryString(dbQuery);
            System.out.println(dbQuery);
            String serverResponse = DBPedia.executeQuery(userQuery);
            return serverResponse;
        }

    }
}
