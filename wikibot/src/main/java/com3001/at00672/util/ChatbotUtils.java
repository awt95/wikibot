package com3001.at00672.util;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.varia.NullAppender;
import org.atteo.evo.inflector.English;

import java.io.*;
import java.util.ArrayList;

public class ChatbotUtils {
    public static void main(String[] args) {
        loadPersonSubclasses();

    }


    public static void loadPersonSubclasses() {
        Writer writer;
        try {
            String people = System.getProperty("user.dir") + "/src/main/resources/bots/wikibot/maps/person2dbo.txt";
            File file = new File(people);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(people)));
            org.apache.log4j.BasicConfigurator.configure();
            String queryString = " PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    " PREFIX yago: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "  SELECT distinct ?type ?class WHERE {\n" +
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
                String type = soln.getLiteral("type").toString().toLowerCase();
                if (type.contains("(")) {
                    // Process result by removing bracketed string
                    int beforeBracket = type.indexOf('(');
                    int afterBracket = type.indexOf(')');
                    type = type.substring(0, beforeBracket).replace(" ", "") + type.substring(afterBracket+1);
                }
                String dboClass = soln.getResource("class").toString();
                // Trim dboClass and add prefix
                String[] dboSplit = dboClass.split("/");
                System.out.println(dboSplit[dboSplit.length -1]);
                String dboString = dboSplit[dboSplit.length - 1];
                //writer.write(String.format("[\"%s\", \"%s\"],\n", type.trim(), dboString));
                writer.write(String.format("%s:%s\n", type.trim(), dboString));
                System.out.println(English.plural(type));
                //writer.write(String.format("[\"%s\", \"%s\"],\n", English.plural(type.trim()), dboString));
                writer.write(String.format("%s:%s\n", English.plural(type.trim()), dboString));


            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
