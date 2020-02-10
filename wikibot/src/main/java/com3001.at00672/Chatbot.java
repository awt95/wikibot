package com3001.at00672;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;

import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;
import org.apache.jena.reasoner.rulesys.builtins.Regex;
import org.apache.log4j.varia.NullAppender;

public class Chatbot {
    public static void main(String[] args) {
        try {
            //org.apache.log4j.BasicConfigurator.configure();
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());

            System.out.println("Starting chatbot");
            String botName = "wikibot";

            String dir = System.getProperty("user.dir");
            Bot bot = new Bot(botName, dir + "/src/main/resources");
            Chat chatSession = new Chat(bot);
            //bot.brain.nodeStats();
            String textLine = "";

            while(true) {
                System.out.print("You: ");
                textLine = IOUtils.readInputTextLine();
                String request = textLine;
                processRequest(request);
                //if (MagicBooleans.trace_mode)
                //  System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                String response = chatSession.multisentenceRespond(request);
                // TODO: Expand to rich content, pictures etc
                response = processResponse(response);
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

    public static void processRequest(String request) {
        if (request.equals("quit") || request.equals("exit")) {
            System.exit(0);
        }
    }

    public static String processResponse(String response) {
        return processOob(response);
    }

    public static String processOob(String oobContent){
        String returnString = "";
        if (oobContent.contains("oob")) {
            Pattern pattern = Pattern.compile("<oob>(.*)</oob>");
            Matcher matcher = pattern.matcher(oobContent);

            if (matcher.find()) {
                String innerContent = matcher.group(1);
                Pattern patternInner = Pattern.compile("<query type=(.*)>(.*)</query>");
                Matcher matcherInner = patternInner.matcher(innerContent);

                if (matcherInner.find()) {
                    if (matcherInner.groupCount() != 2) { throw new IllegalArgumentException("Invalid number of arguments");}
                    String queryType = matcherInner.group(1).replaceAll("\"","");
                    String queryContent = matcherInner.group(2).replaceAll("\"","");

                    // Now do something with this
                    switch (queryType) {
                        case "person": returnString = DBPedia.UserQuery(queryContent); break;

                        default: returnString = "I couldn't find the answer to that"; break;
                    }
                } else {
                    returnString = "Invalid arguments matched";
                }
            } else {
                returnString = "Invalid arguments matched";
            }
        }
        return returnString;
    }
}