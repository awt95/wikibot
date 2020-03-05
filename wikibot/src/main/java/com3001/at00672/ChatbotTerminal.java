package com3001.at00672;

import com3001.at00672.model.UserQuery;
import com3001.at00672.service.ChatbotService;
import org.apache.commons.text.WordUtils;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

import java.util.HashMap;

public class ChatbotTerminal {

    //private static UserQuery userQuery;
    //private static ChatbotService chatbotService;

    public static void main(String[] args) {
        try {
            ChatbotService chatbotService = new ChatbotService();
            org.apache.log4j.BasicConfigurator.configure();
            //org.apache.log4j.BasicConfigurator.configure(new NullAppender());

            System.out.println("Starting chatbot");
            String botName = "wikibot";

            String dir = System.getProperty("user.dir");
            Bot bot = new Bot(botName, dir + "/src/main/resources");
            //bot.brain.nodeStats();
            String textLine = "";

            while (true) {

                System.out.print("You: ");
                textLine = IOUtils.readInputTextLine();
                String request = textLine;
                //if (MagicBooleans.trace_mode)
                //  System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                String response = chatbotService.chatSession.multisentenceRespond(request);
                UserQuery userQuery = new UserQuery(chatbotService.chatSession.predicates);
                if (request.equals("quit") || request.equals("exit")) {
                    System.exit(0);
                }

                // TODO: Expand to rich content, pictures etc
                response = chatbotService.processResponse(userQuery, response);
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

}
