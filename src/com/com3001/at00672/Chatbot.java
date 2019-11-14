package com.com3001.at00672;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

import java.io.File;

public class Chatbot {
    private static final boolean TRACE_MODE = false;
    static String botName = "super"; // using test bot
    public static void main(String[] args) {
        System.out.println("Starting chatbot");

        Bot bot = new Bot(botName, "/home/alex/COM3001/src/com/com3001/at00672/resources");
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

    }
}
