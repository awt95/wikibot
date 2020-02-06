package com3001.at00672;
import java.io.File;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

public class Chatbot {
    public static void main(String[] args) {
        try {
            org.apache.log4j.BasicConfigurator.configure();

            System.out.println("Starting chatbot");
            String botName = "super"; // using test bot
            Bot bot = new Bot(botName, "/home/alex/com3001_at00672/wikibot/src/main/resources");
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
}
