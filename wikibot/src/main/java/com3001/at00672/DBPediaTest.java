package com3001.at00672;

import com3001.at00672.model.DBpediaResource;
import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DBPediaTest {

    public static void main(String[] args) {
        DBpediaResource resource = new DBpediaResource();
        HashMap<String,String> properties = new HashMap<>();
        resource.setUri("http://dbpedia.org/resource/Alan_Turing");
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
                " SELECT ?property ?value \n" +
                "WHERE \n" +
                "  {\n" +
                "     { <http://dbpedia.org/resource/Alan_Turing>  ?property  ?value . " +
                        "FILTER(!isLiteral(?value) || langMatches(lang(?value), \"EN\")) }\n" +
                "  }";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        String service = "http://dbpedia.org/sparql";
        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
        ResultSet results = qe.execSelect();
        List<DBpediaResource> resourceList = new ArrayList<>();
        // TODO: loop through all results
        while (results.hasNext()) {
            try {
                QuerySolution soln = results.next();
                RDFNode property = soln.get("property");
                RDFNode value = soln.get("value");
                if (property != null) {
                    if (value != null) {
                        DBpediaResource dBpediaResource;
                        //resourceList.add(new DBpediaResource(property.asResource(), value.asResource()));
                        // TODO: put this in DBpedia Resource
                        if (value.isLiteral()) {
                            resourceList.add(new DBpediaResource(property.asResource(), value.asLiteral()));
                        } else if (value.isResource()) {
                            resourceList.add(new DBpediaResource(property.asResource(), value.asResource()));
                        }

                    }
                }
            }
            catch (NullPointerException npe) {
                System.out.println("Null pointer found, continuing...");
            }

            System.out.println(resourceList.size());


        }

    }
}
