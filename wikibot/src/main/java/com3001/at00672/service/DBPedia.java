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
import java.util.*;

public class DBPedia {
    public ArrayList<String> queryKeywords = new ArrayList<>(Arrays.asList("query", "list", "list_conditional"));

    public static void main(String[] args) {
        //TestConnection();
        //ExampleQuery();
    }

    public static void TestConnection() {
        //org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
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
            else
                executePersonQuery(userQuery, botResponse);
        } else if (userQuery.get("topic").equalsIgnoreCase("country")) {
            executeCountryQuery(userQuery, botResponse);
        } else {
            botResponse.setContent("Something went wrong");
        }
    }
    // TODO: Get infobox from wikipedia?
    public static void executeAbstractPersonQuery(UserQuery userQuery, Message botResponse) {
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            // TODO: loop through all results
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
            // TODO: loop through all results
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

    //TODO: Some country comments get truncated
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

    public static void  executeAgeQuery(UserQuery userQuery, Message botResponse) {
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            // TODO: loop through all results
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode resource = solution.get("person");
                RDFNode birthDate = solution.get("birthDate");
                RDFNode deathDate = solution.get("deathDate");
                if ((birthDate.asLiteral().getDatatype() instanceof XSDDateType) && (deathDate.asLiteral().getDatatype() instanceof XSDDateType)) {
                    DateFormat xsdFormat = new SimpleDateFormat("yyyy-MM-dd");

                } else {
                    throw new IllegalArgumentException("Date is wrong type");
                }

                // Convert values

                // If they have died

                // Calculate age today

                // Generate response
            } else {
                botResponse.setContent("Something went wrong");
            }
        } catch (Exception e) {
            botResponse.setContent("Something went wrong");
            e.printStackTrace();
        }
    }

    public static void ExampleQuery() {
        //org.apache.log4j.BasicConfigurator.configure();

        String str = "Turing";
        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                " PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
                " PREFIX dbo: <http://dbpedia.org/ontology/>" +
                " SELECT ?uri ?txt WHERE {" +
                " ?uri rdfs:label ?txt ." +
                " ?txt <bif:contains> \"'" + str + "'\" . } LIMIT 10";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        try {
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                System.out.println(soln);
            }
        } finally {
            qexec.close();
        }
    }
}
