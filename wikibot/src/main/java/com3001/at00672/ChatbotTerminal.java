/**
 * ChatbotTerminal
 * - Terminal application for running chatbot
 * @author Alex Turner
 */
package com3001.at00672;

import com3001.at00672.model.*;
import com3001.at00672.service.ChatbotService;
import org.apache.commons.text.WordUtils;
import org.apache.log4j.varia.NullAppender;
import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.HashMap;

public class ChatbotTerminal {


    public static void main(String[] args) {
        try {
            ChatbotService chatbotService = new ChatbotService();
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            chatbotService.bot.brain.nodeStats();
            System.out.println("Starting chatbot");
            String textLine = "";

            while (true) {

                System.out.print("You: ");
                textLine = IOUtils.readInputTextLine();
                String request = textLine;
                Message botResponse = new Message(chatbotService.chatSession.multisentenceRespond(request), Sender.BOT);
                UserQuery userQuery = new UserQuery(chatbotService.chatSession.predicates);
                if (request.equals("quit") || request.equals("exit")) {
                    System.exit(0);
                }

                chatbotService.processResponse(userQuery, botResponse);

                System.out.println();
                System.out.println("Bot: ");
                for (MessageItem item : botResponse.getMessageItems()) {
                    System.out.println(WordUtils.wrap(item.getContent(), 80));

                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
