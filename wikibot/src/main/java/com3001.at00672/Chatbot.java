package com3001.at00672;
import java.io.File;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

public class Chatbot {
    public static void main(String[] args) {
        try {
            org.apache.log4j.BasicConfigurator.configure();

            System.out.println("Starting chatbot");
            //String botName = "super"; // using test bot
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
                if (MagicBooleans.trace_mode)
                    System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                String response = chatSession.multisentenceRespond(request);
                processResponse(response);
                while (response.contains("&lt;"))
                    response = response.replace("&lt;", "<");
                while (response.contains("&gt;"))
                    response = response.replace("&gt;", ">");
                System.out.println("Bot: " + response);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processResponse(String response) {
        processOob(response);
    }

    public static void processOob(String oobContent) {
        if (oobContent.contains("person")) {
            //String queryResponse = DBPedia.UserQuery();
            //System.out.println(queryResponse);
            String query = QueryBuilder.PersonQuery("My query");
            System.out.println(query);
            DBPedia.UserQuery(query);
        }
        // Remove tags
    }
}
