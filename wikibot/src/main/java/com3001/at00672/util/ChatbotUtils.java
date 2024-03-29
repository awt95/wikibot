/**
 * ChatbotUtils
 * - Utility functions for loading data into sets/maps from DBPedia
 */
package com3001.at00672.util;

import org.alicebot.ab.utils.IOUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.varia.NullAppender;
import org.atteo.evo.inflector.English;

import java.io.*;
import java.util.ArrayList;

public class ChatbotUtils {
    public static void main(String[] args) {
        while(true) {
            System.out.println("Select option");
            System.out.println("Note this will overwrite existing files");
            System.out.println("[1] : Load Countries");
            System.out.println("[2] : Load Country Resources");
            System.out.println("[3] : Load Person subclasses");
            System.out.print("Option: ");

            String input = IOUtils.readInputTextLine();
            System.out.println();
            switch (input) {
                case "1": loadCountries();break;
                case "2": loadCountryDbr();break;
                case "3": loadPersonSubclasses(); break;
                default: System.out.println("Invalid option");break;
            }
        }
    }

    //TODO: Rewrite to include certain countries e.g. England
    public static void loadCountries() {
        Writer writer;
        try {
            String people = System.getProperty("user.dir") + "/src/main/resources/bots/wikibot/sets/countries.txt";
            File file = new File(people);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(people)));
            org.apache.log4j.BasicConfigurator.configure();
            String queryString = " PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    " SELECT DISTINCT ?country ?name\n" +
                    " WHERE {\n" +
                    " ?country a dbo:Country.\n" +
                    " ?country dbo:capital ?capital.\n" +
                    " ?country rdfs:label ?name\n" +
                    " FILTER NOT EXISTS { ?country dbo:dissolutionYear ?yearEnd }\n" +
                    " FILTER langMatches(lang(?name), 'en')\n" +
                    "} ORDER BY ?country";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String country = soln.getLiteral("name").getLexicalForm();
                writer.write(country + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCountryDbr() {
        Writer writer;
        try {
            String people = System.getProperty("user.dir") + "/src/main/resources/bots/wikibot/maps/country2dbr.txt";
            File file = new File(people);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(people)));
            org.apache.log4j.BasicConfigurator.configure();
            String queryString = " PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    " SELECT DISTINCT ?country ?name\n" +
                    " WHERE {\n" +
                    " ?country a dbo:Country.\n" +
                    " ?country dbo:capital ?capital.\n" +
                    " ?country rdfs:label ?name\n" +
                    " FILTER NOT EXISTS { ?country dbo:dissolutionYear ?yearEnd }\n" +
                    " FILTER langMatches(lang(?name), 'en')\n" +
                    "} ORDER BY ?country";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            // TODO: loop through all results
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String country = soln.getLiteral("name").getLexicalForm();
                String resourceString = soln.getResource("country").toString();
                String[] dbrSplit = resourceString.split("/");
                // Get last part of resource string
                System.out.println(dbrSplit[dbrSplit.length -1]);
                String dbrString = dbrSplit[dbrSplit.length - 1];

                writer.write(country + ":" + dbrString + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
