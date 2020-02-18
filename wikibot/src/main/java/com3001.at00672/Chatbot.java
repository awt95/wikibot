package com3001.at00672;
import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.WordUtils;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;
import org.apache.jena.reasoner.rulesys.builtins.Regex;
import org.apache.log4j.varia.NullAppender;

import static com3001.at00672.QueryBuilder.*;

public class Chatbot {

    private static UserQuery userQuery;

    //TODO: init class

    public static void main(String[] args) {
        try {
            org.apache.log4j.BasicConfigurator.configure();
            //org.apache.log4j.BasicConfigurator.configure(new NullAppender());

            System.out.println("Starting chatbot");
            String botName = "wikibot";

            String dir = System.getProperty("user.dir");
            Bot bot = new Bot(botName, dir + "/src/main/resources");
            Chat chatSession = new Chat(bot);
            //bot.brain.nodeStats();
            String textLine = "";

            while(true) {

                System.out.print("You: ");
                textLine = IOUtils.readInputTextLine();
                String request = textLine;
                //if (MagicBooleans.trace_mode)
                //  System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                String response = chatSession.multisentenceRespond(request);
                processRequest(request, chatSession.predicates);

                // TODO: Expand to rich content, pictures etc
                //response = processResponse(response);
                while (response.contains("&lt;"))
                    response = response.replace("&lt;", "<");
                while (response.contains("&gt;"))
                    response = response.replace("&gt;", ">");
                System.out.println();
                System.out.println("Bot: ");
                System.out.println(WordUtils.wrap(response, 80));
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processRequest(String request, Predicates predicates) {
        // utilities

        UserQuery userQuery = new UserQuery();
        userQuery.setTopic(predicates.get("topic"));
        userQuery.setIri(predicates.get("property"));
        userQuery.setQueryType(predicates.get("queryType"));
        userQuery.setValue(WordUtils.capitalize(predicates.get("value")));
        String[] properties = userQuery.getIri().split(":");

        if(properties.length == 2) {
            userQuery.setNamespace(properties[0]);
            userQuery.setProperty(properties[1]);
        }

        System.out.println(String.format("TOPIC: %s, PROPERTY: %s, QUERYTYPE: %s VALUE: %s", userQuery.getTopic(), userQuery.getProperty(), userQuery.getQueryType(), userQuery.getValue()));

        if (request.equals("quit") || request.equals("exit")) {
            System.exit(0);
        }
    }

    public static String processResponse(String response) {
        System.out.println(userQuery.toString());
        System.out.println(String.format("TOPIC: %s, PROPERTY: %s, VALUE: %s", userQuery.getTopic(), userQuery.getProperty(), userQuery.getValue()));
        // generate query
        String dbQuery = generateQuery(userQuery);
        userQuery.setQueryString(dbQuery);
        System.out.println(dbQuery);
        String serverResponse = DBPedia.executeQuery(userQuery);
        return serverResponse;
    }

}
