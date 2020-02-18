package com3001.at00672;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ChatbotUtils {
    public static void main(String[] args) throws IOException {
        System.out.println("Loading countries set");
        String dir = System.getProperty("user.dir");
        String countriesPath = dir + "/src/main/resources/bots/wikibot/sets/countries.txt";
        System.out.println(countriesPath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(countriesPath));
        // setup connection
        String countriesQuery = " PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                " PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                " PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                " SELECT ?place, ?label WHERE {\n" +
                " ?place a dbo:Country .\n" +
                " ?place rdfs:label ?label .\n" +
                " FILTER langMatches(lang(?label), 'en')}" +
                " ORDER BY ASC(?label) LIMIT 100 ";
        Query query = QueryFactory.create(countriesQuery);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        ResultSet results = qexec.execSelect();
        // TODO: loop through all results
        ArrayList<String> resultsList = new ArrayList();
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            System.out.println(solution.toString());
            writer.write(solution.toString());
        }
        writer.close();
    }
}
