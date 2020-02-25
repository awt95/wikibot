package com3001.at00672;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.ext.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.jena.ext.xerces.xs.datatypes.XSDateTime;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
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

    public static String executeQuery(UserQuery userQuery) {
        String returnString = "";
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            //System.out.println(queryString);
            Query query = QueryFactory.create(userQuery.getQueryString());
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            // TODO: loop through all results
            ArrayList<String> resultsList = new ArrayList();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode node = solution.get(userQuery.getProperty());
                String result = processResource(node);
                resultsList.add(result);
            }
            // form return
            if (resultsList.size() > 1) {

            } else {
                returnString = resultsList.get(0);
            }
            returnString = resultsList.toString();

        } catch (Exception e) {
            e.printStackTrace();
            returnString = "Sorry I don't know. No results found.";
        }
        return returnString;
    }

    private static String processResource(RDFNode node) throws ParseException {
        String result = "";
        // Check if it as resource
        if (node.isResource()) {
            Resource r = node.asResource();
            String uri = r.getURI();
            if (uri.contains("dbpedia.org")) {
                if (uri.contains("resource")) {
                    RDFParser parser = RDFParser.create().source(uri).base("http://dbpedia.org/resource").build();
                    String[] urlParts = uri.split("/");
                    String resourceName = urlParts[urlParts.length - 1];
                    result = resourceName.replace("_", " ");
                }
            } else {
                result = "Could not find resource value";
            }
        } else if (node.isLiteral()) {
            RDFDatatype dtype = node.asLiteral().getDatatype();
            if (dtype instanceof XSDDateType) {
                DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inFormat.parse(node.asLiteral().getLexicalForm());
                DateFormat outFormat = new SimpleDateFormat("dd MMM yyyy");
                return outFormat.format(date);
            } else {
                result = node.asLiteral().getString();
            }
        } else {
            result = "Error occurred parsing node";
        }
        return result;
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
