package com3001.at00672;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.varia.NullAppender;

import java.io.*;
import java.util.ArrayList;

public class ChatbotUtils {
    public static void main(String[] args) {
        loadPersonSubclasses();
    }

    public static void loadPersonSubclasses() {
        Writer writer;
        try {
            String people = System.getProperty("user.dir") + "/src/main/resources/bots/wikibot/sets/person_type.txt";
            File file = new File(people);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(people)));
            org.apache.log4j.BasicConfigurator.configure();
            String queryString = " PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    " PREFIX yago: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "  SELECT distinct ?type WHERE {\n" +
                    " ?class rdfs:subClassOf* dbo:Person .\n" +
                    " ?class rdfs:label ?label\n" +
                    " FILTER langMatches(lang(?label), 'en') ." +
                    " BIND (STR(?label)  AS ?type)." +
                    " }";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            // TODO: loop through all results
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                System.out.println(soln.getLiteral("type"));
                writer.write(soln.getLiteral("type").toString().toLowerCase() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
