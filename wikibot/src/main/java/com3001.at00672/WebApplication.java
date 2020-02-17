package com3001.at00672;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class WebApplication {
    private static final Logger logger = Logger.getLogger(WebController.class);

    public static void main(String[] args) {
        org.apache.log4j.BasicConfigurator.configure();
        // initialise chatbot
        String botName = "wikibot";

        String dir = System.getProperty("user.dir");
        Bot bot = new Bot(botName, dir + "/src/main/resources");
        Chat chatSession = new Chat(bot);

        // start web app
        SpringApplication.run(WebApplication.class, args);
    }
}
