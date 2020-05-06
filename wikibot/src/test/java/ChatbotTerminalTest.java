import com3001.at00672.model.UserQuery;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Predicates;
import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.log4j.varia.NullAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatbotTerminalTest {

    private static Bot bot;
    private static Chat chatSession;

    @BeforeAll
    static void initialize() {
        //org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        System.out.println("Starting chatbot");
        String botName = "wikibot";

        String dir = System.getProperty("user.dir");
        bot = new Bot(botName, dir + "/src/main/resources");
        chatSession = new Chat(bot);
        assertNotNull(bot, "Bot is running");
        //bot.brain.nodeStats();*/
    }

    @Test
    void testConnection() {
        String service = "http://dbpedia.org/sparql";
        String query = "ASK { }";
        boolean serviceIsUp;
        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
        try {
            serviceIsUp = qe.execAsk();
        } catch (QueryExceptionHTTP e) {
            serviceIsUp = false;
        } finally {
            qe.close();
        }

        assertEquals(true, serviceIsUp, "Service is up");
    }

    @ParameterizedTest(name = "query #{index} with [{arguments}]")
    @CsvSource({
            "Person, rdfs:comment, rdfs, comment, Alan Turing",
            "Person, dbo:birthplace, dbo, birthplace, Alan Turing",
            "Person, dbo:birthdate, dbo, birthdate, Alan Turing"
    })
    void userQueryTest(String topic, String iri, String namespace, String property, String value) {
        Predicates predicates = new Predicates();
        predicates.put("topic", topic);
        predicates.put("iri", iri);
        predicates.put("namespace", namespace);
        predicates.put("property", property);
        predicates.put("value", value);
        UserQuery userQuery = new UserQuery(predicates);

        System.out.println(userQuery.toString());
        assertEquals(userQuery.get("topic"), topic, "Passed topic assertion");
        assertEquals(userQuery.get("iri"), iri, "Passed iri assertion");
        assertEquals(userQuery.get("namespace"), namespace, "Passed namespace assertion");
        assertEquals(userQuery.get("property"), property, "Passed property assertion");
        assertEquals(userQuery.get("value"), value, "Passed value assertion");
    }
}
