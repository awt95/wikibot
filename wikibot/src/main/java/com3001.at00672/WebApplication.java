package com3001.at00672;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories
public class WebApplication extends SpringBootServletInitializer {
    private static final Logger logger = Logger.getLogger(WebController.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }

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
