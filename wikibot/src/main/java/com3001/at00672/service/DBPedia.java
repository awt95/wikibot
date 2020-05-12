package com3001.at00672.service;

import com3001.at00672.model.*;
import org.apache.catalina.User;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.log4j.varia.NullAppender;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBPedia {
    public ArrayList<String> queryKeywords = new ArrayList<>(Arrays.asList("query", "list", "list_conditional"));

    public static void main(String[] args) {
        //TestConnection();
        ExampleQuery();
    }

    public static void TestConnection() {
        org.apache.log4j.BasicConfigurator.configure();
        String service = "http://dbpedia.org/sparql";
        String query = "ASK { }";
        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
        try {
            if (qe.execAsk()) {
                System.out.println(service + " is UP");
            } // end if
        } catch (QueryExceptionHTTP e) {
            System.out.println(service + " is DOWN");
        } finally {
            qe.close();
        }
    }

    public static void executeQuery(UserQuery userQuery, Message botResponse) {
        if (userQuery.get("topic").equalsIgnoreCase("person")) {
            if (botResponse.getMessageType().equals(MessageType.ABSTRACT))
                executeAbstractPersonQuery(userQuery, botResponse);
            else if (userQuery.get("function").equalsIgnoreCase("calculateAge"))
                executeAgeQuery(userQuery, botResponse);
            else
                executePersonQuery(userQuery, botResponse);
        } else if (userQuery.get("topic").equalsIgnoreCase("country")) {
            executeCountryQuery(userQuery, botResponse);
        } else {
            botResponse.setContent("Something went wrong");
        }
    }
    public static void executeAbstractPersonQuery(UserQuery userQuery, Message botResponse) {
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode resource = solution.get("person");
                String resourceURI = resource.asResource().getURI();
                String name = solution.get("name").asLiteral().getString();
                String content = solution.get("comment").asLiteral().getString();
                String thumbnail = solution.get("thumbnail").asResource().getURI(); //?
                botResponse.setTitle(name);
                botResponse.setContent(content);
                botResponse.setImageURL(thumbnail);
            } else {
                botResponse.setContent("Something went wrong");
            }
        } catch (Exception e) {
            botResponse.setContent("Something went wrong");
            e.printStackTrace();
        }
    }

    public static void executePersonQuery(UserQuery userQuery, Message botResponse) {
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode node = solution.get(userQuery.get("property"));
                RDFNode resource = solution.get("person");
                String resourceURI = null;
                if (resource != null && resource.isResource()) {
                    resourceURI = resource.asResource().getURI();
                }
                String result = processResource(node);
                //processResource(node);
                botResponse.addMessageItem(new MessageItem(resourceURI, result));
            }
            if (botResponse.getMessageItems().size() == 0) {
                botResponse.addMessageItem(new MessageItem("I don't know yet."));
            }
        } catch (Exception e) {
            botResponse.addMessageItem(new MessageItem("Something went wrong."));
        }
    }

    public static void executeCountryQuery(UserQuery userQuery, Message botResponse) {
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode node = solution.get(userQuery.get("property"));
                String result = processResource(node);
                botResponse.addMessageItem(new MessageItem(result));
            }
            if (botResponse.getMessageItems().size() == 0) {
                botResponse.addMessageItem(new MessageItem("I don't know yet."));
            }
        } catch (Exception e) {
            botResponse.addMessageItem(new MessageItem("Something went wrong."));
        }
    }

    private static String processResource(RDFNode node) throws ParseException {
        String result = "";
        //String uri = node.ge;
        // Check if it as resource
        if (node.isResource()) {
            Resource r = node.asResource();
            String rURI = r.getURI();
            if (rURI.contains("dbpedia.org")) {
                if (rURI.contains("resource")) {
                    RDFParser parser = RDFParser.create().source(rURI).base("http://dbpedia.org/resource").build();
                    String[] urlParts = rURI.split("/");
                    String resourceName = urlParts[urlParts.length - 1];
                    result = resourceName.replace("_", " ");
                }
            } else {
                result = "Could not find resource value";
                //uri = "RESPONSE";
            }
        } else if (node.isLiteral()) {
            //uri = node.u
            RDFDatatype dtype = node.asLiteral().getDatatype();
            if (dtype instanceof XSDDateType) {
                DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inFormat.parse(node.asLiteral().getLexicalForm());
                DateFormat outFormat = new SimpleDateFormat("dd MMM yyyy");
                result = outFormat.format(date);
            } else {
                result = node.asLiteral().getString();
            }
        } else {
            result = "Error occurred parsing node";
        }
        return result;
        //String[] results = new String[]{"test", "test"};
        //return results;
    }

    public static void executeAgeQuery(UserQuery userQuery, Message botResponse) {
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String personName = solution.get("name").asLiteral().getString();
                RDFNode rdfBirthDate = solution.get("birthDate");
                RDFNode rdfDeathDate = solution.get("deathDate");

                LocalDate todayDate = LocalDate.now();
                int age;

                if (rdfBirthDate.asLiteral().getDatatype() instanceof XSDDateType) {
                    String birthDateString = rdfBirthDate.asLiteral().getLexicalForm();
                    LocalDate birthDate = parseDateString(birthDateString);
                    if (rdfDeathDate == null) {
                        // Still alive
                        age = Period.between(birthDate, todayDate).getYears();
                        botResponse.addMessageItem(new MessageItem(String.format("%s is %s years old", personName, age)));
                    } else if (rdfDeathDate.asLiteral().getDatatype() instanceof XSDDateType) {
                        // Dead
                        String deathDateString = rdfDeathDate.asLiteral().getLexicalForm();
                        LocalDate deathDate = parseDateString(deathDateString);
                        age = Period.between(birthDate, deathDate).getYears();
                        botResponse.addMessageItem(new MessageItem(String.format("%s was %s years old when they died in %s", personName, age, deathDate.getYear())));
                    } else {
                        throw new IllegalArgumentException("Date is wrong type");
                    }
                }
            } else {
                botResponse.setContent("Something went wrong");
            }
        } catch (Exception e) {
            botResponse.setContent("Something went wrong");
            e.printStackTrace();
        }
    }

    private static LocalDate parseDateString(String dateString) {
        String[] dateSplit = dateString.split("-");
        if (dateSplit.length != 3)
            throw new IllegalArgumentException("Date is wrong type");
        // Manually parse date because of error caused by inconsistent lengths
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        return LocalDate.of(year, month, day);
    }

    public static void ExampleQuery() {
        org.apache.log4j.BasicConfigurator.configure();
        String queryString =
                "PREFIX dbo: <http://dbpedia.org/ontology/>" +
                "PREFIX dbr: <http://dbpedia.org/resource/>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "SELECT ?person ?birthPlace ?name WHERE {" +
                "?person a dbo:MusicalArtist ." +
                "?person rdfs:label ?name ." +
                "?person dbo:birthPlace ?birthPlace ." +
                "filter(?birthPlace = dbr:Manchester)" +
                "filter(langMatches(lang(?name), 'en'))" +
                "} LIMIT 50 ";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.getLiteral("name").getString();
                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            qexec.close();
        }
    }
}
