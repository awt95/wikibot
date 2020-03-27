import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatbotTerminalQueryTest {

    private static Bot bot;
    private static Chat chatSession;

    @BeforeAll
    static void initialize() {
/*        //org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        System.out.println("Starting chatbot");
        String botName = "wikibot";

        String dir = System.getProperty("user.dir");
        bot = new Bot(botName, dir + "/src/main/resources");
        chatSession = new Chat(bot);
        assertNotNull(bot, "Bot is running");
        //bot.brain.nodeStats();*/
    }
    @ParameterizedTest(name = "query #{index} with [{arguments}]")
    @CsvSource({
            "Person, rdfs:comment, rdfs, comment, Alan Turing",
            "Person, dbo:birthplace, dbo, birthplace, Alan Turing",
            "Person, dbo:birthdate, dbo, birthdate, Alan Turing"
    })
    void runMultipleQueries(String topic, String iri, String namespace, String property, String value) {
/*        UserQuery userQuery = new UserQuery();
        userQuery.setTopic(topic);
        userQuery.setIri(iri);
        userQuery.setNamespace(namespace);
        userQuery.setProperty(property);
        userQuery.setValue(value);
        System.out.println(userQuery.toString());
        assertEquals(userQuery.getTopic(), topic, "Passed UserQuery assertion");
        assertEquals(userQuery.getValue(), value, "Value matches expected value");
        // Generate query
        String queryString = generateQuery(userQuery);
        userQuery.setQueryString(queryString);
        // Execute query
        String response = DBPedia.executeQuery(userQuery);
        System.out.println(response);*/
    }
}
