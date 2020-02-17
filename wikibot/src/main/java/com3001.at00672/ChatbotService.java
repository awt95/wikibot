package com3001.at00672;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;

import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {
    @Autowired
    private ChatRepository chatRepository;
    private Bot bot;
    private Chat chatSession;

    public ChatbotService() {
        String botName = "wikibot";
        String dir = System.getProperty("user.dir");
        bot = new Bot(botName, dir + "/src/main/resources");
        chatSession = new Chat(bot);
    }

    public Message chatbotRequest(Message request) {
        Message response = new Message("Bot: " + bot.name, Sender.BOT);
        return response;
    }
}
