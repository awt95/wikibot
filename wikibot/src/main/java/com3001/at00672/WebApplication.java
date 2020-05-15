package com3001.at00672;
import com3001.at00672.controller.WebController;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
        // start web app
        SpringApplication.run(WebApplication.class, args);
    }
}
