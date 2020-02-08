package com3001.at00672;
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
        SpringApplication.run(WebApplication.class, args);
    }
}
