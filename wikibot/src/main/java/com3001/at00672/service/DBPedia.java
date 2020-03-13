package com3001.at00672.service;

import com3001.at00672.model.ChatContext;
import com3001.at00672.model.MessageItem;
import com3001.at00672.model.UserQuery;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBPedia {

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

    public static List<MessageItem> executeQuery(UserQuery userQuery) {
        String returnString = "";
        List<MessageItem> resultsList = new ArrayList<>();
        //ArrayList<String> resultsList = new ArrayList();
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

                String result = processResource(node);
                String resourceURI = null;
                if (resource != null && resource.isResource()) {
                    resourceURI = resource.asResource().getURI();
                    //context.setSubject(resource.asResource().getLocalName());
                }
                resultsList.add(new MessageItem(resourceURI, result));
            }
            if (resultsList.size() == 0) {
                resultsList.add(new MessageItem(null, "I don't know that yet."));
            }

        } catch (Exception e) {
            resultsList = new ArrayList<>();
            resultsList.add(new MessageItem(null, "Something went wrong"));
        }
        // Chatcontext stuff
        return resultsList;
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
