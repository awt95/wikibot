package com3001.at00672;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.apache.log4j.varia.NullAppender;

public class DBPedia {

    public static void main(String[] args) {

    }

    public static void TestConnection() {
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

    public static String UserQuery(String queryString) {
        try {
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                System.out.println(soln);
            }
            return "Hope that answers your query";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I don't know";
        }
    }

    public static void ExampleQuery() {
        //org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

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
