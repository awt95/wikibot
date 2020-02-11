package com3001.at00672;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;
import org.apache.jena.reasoner.rulesys.builtins.Regex;
import org.apache.log4j.varia.NullAppender;

import static com3001.at00672.QueryBuilder.*;

public class Chatbot {

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
                processRequest(request);
                //if (MagicBooleans.trace_mode)
                //  System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                String response = chatSession.multisentenceRespond(request);
                // TODO: Expand to rich content, pictures etc
                response = processResponse(response, chatSession.predicates);
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

    public static void processRequest(String request) {
        if (request.equals("quit") || request.equals("exit")) {
            System.exit(0);
        }
    }

    public static String processResponse(String response, Predicates predicates) {
        String topic = predicates.get("topic");
        String query = predicates.get("query");
        String value = predicates.get("value");
        System.out.println(String.format("TOPIC: %s, QUERY: %s, VALUE: %s", topic, query, value));
        // generate query
        String dbQuery = GenerateQuery(topic, query, value);
        System.out.println(dbQuery);
        if (dbQuery.equals(null)) {
            response = "An error occurred in my QueryBuilder";
        } else {
            // Run query
            response = DBPedia.executeQuery(dbQuery);
        }
        return response;
    }

}
