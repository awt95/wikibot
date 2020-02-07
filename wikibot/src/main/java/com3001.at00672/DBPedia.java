package com3001.at00672;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.log4j.varia.NullAppender;

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

    public static String UserQuery(String userQuery) {
        String returnString = "";
        try {
            org.apache.log4j.BasicConfigurator.configure(new NullAppender());
            String queryString = QueryBuilder.PersonQuery(userQuery);
            //System.out.println(queryString);
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Literal l = soln.getLiteral("comment");
                returnString = l.getString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            returnString = "Sorry I don't know. No results found.";
        }
        return returnString;
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
