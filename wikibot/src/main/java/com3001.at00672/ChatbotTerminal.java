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

public class ChatbotTerminal {

    private static UserQuery userQuery;
    private static ChatbotService chatbotService;

    public static void main(String[] args) {
        try {
            chatbotService = new ChatbotService();
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
                chatbotService.processRequest(request, chatbotService.chatSession.predicates);

                // TODO: Expand to rich content, pictures etc
                response = chatbotService.processResponse(response);
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
