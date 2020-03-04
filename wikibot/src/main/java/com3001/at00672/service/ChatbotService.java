package com3001.at00672;

import java.util.ArrayList;
import java.util.Arrays;

import com3001.at00672.model.Message;
import com3001.at00672.model.Sender;
import org.apache.commons.text.WordUtils;

import org.alicebot.ab.*;
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
    public ArrayList<String> queryKeywords = new ArrayList<>(Arrays.asList("query", "list"));

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
        userQuery.setIri(predicates.get("iri"));
        userQuery.setProperty(predicates.get("property"));
        userQuery.setFunction(predicates.get("function"));
        userQuery.setValue(WordUtils.capitalize(predicates.get("value")));

        if (request.equals("quit") || request.equals("exit")) {
            System.exit(0);
        }
    }

    public String processResponse(String response) {
        if (queryKeywords.contains(userQuery.getFunction())) {
            System.out.println(userQuery.toString());
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
