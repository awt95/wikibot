package com3001.at00672;

import com3001.at00672.model.*;
import com3001.at00672.service.ChatbotService;
import org.apache.commons.text.WordUtils;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

import java.util.HashMap;

public class ChatbotTerminal {

    //private static UserQuery userQuery;
    //private static ChatbotService chatbotService;
    private static ChatContext chatContext;

    public static void main(String[] args) {
        try {
            ChatbotService chatbotService = new ChatbotService();
            chatContext = new ChatContext();
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
                Message botResponse = new Message(chatbotService.chatSession.multisentenceRespond(request), Sender.BOT);
                UserQuery userQuery = new UserQuery(chatbotService.chatSession.predicates);
                if (request.equals("quit") || request.equals("exit")) {
                    System.exit(0);
                }

                // TODO: Expand to rich content, pictures etc
                chatbotService.processResponse(userQuery, botResponse);

                System.out.println();
                System.out.println("Bot: ");
                for (MessageItem item : botResponse.getMessageItems()) {
                    System.out.println(WordUtils.wrap(item.getContent() + ", " + item.getUri(), 80));

                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
