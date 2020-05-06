package com3001.at00672.service;

import com3001.at00672.model.UserQuery;

import java.util.ArrayList;
import java.util.Arrays;

import static org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf.iri;


public class QueryBuilder {
    public static ArrayList<String> predicates = new ArrayList<>(Arrays.asList("influenced", "influencedBy"));

    public static String generateQuery(UserQuery userQuery) {
        String result = "";
        switch (userQuery.get("function")) {
            case "query": result = generateUserQuery(userQuery); break;
            case "abstract": result = generateAbstractQuery(userQuery); break;
            case "list": result = generateListQuery(userQuery); break;
            case "list_conditional": result = generateListConditionalQuery(userQuery); break;
            case "calculateAge": result = generateAgeQuery(userQuery); break;
            default: result = "";
        }
        return result;

    }

    public static String generateUserQuery(UserQuery userQuery) {
        String result = "";
        if (userQuery.get("topic").equalsIgnoreCase("country")) {
            result = generateCountryQuery(userQuery);
        } else if (userQuery.get("topic").equalsIgnoreCase("person")) {
            result = generatePersonQuery(userQuery);
        }
        return result;
    }

    public static String generateAbstractQuery(UserQuery userQuery) {
        System.out.println("Abstract query");
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(" SELECT DISTINCT ?person ?thumbnail ?name ?comment WHERE {");
        sb.append("  ?person a dbo:Person .");
        sb.append("?person foaf:name ?name .");
        sb.append(String.format("  ?name <bif:contains> \"'%s'\" .", userQuery.get("value")));
        sb.append("  ?person rdfs:comment ?comment .");
        sb.append("  ?person dbo:thumbnail ?thumbnail .");
        sb.append(" FILTER  langMatches(lang(?comment), 'en')");
        sb.append("} LIMIT 1");

        System.out.println(sb.toString());

        return sb.toString();
    }

    // TODO: Implementation of age
    public static String generateAgeQuery(UserQuery userQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append("SELECT ?person ?name ?birthDate ?deathDate WHERE {");
        sb.append("?person a dbo:Person .");
        sb.append("?person foaf:name ?name .");
        sb.append(String.format("?name <bif:contains> \"'%s'\" .", userQuery.get("value")));
        sb.append("?person dbo:birthDate ?birthDate .");
        sb.append(  "OPTIONAL{?person dbo:deathDate ?deathDate} .");
        sb.append("FILTER langMatches(lang(?name), 'en')");
        sb.append("} LIMIT 1");
        return sb.toString();
    }
    public static String generatePersonQuery(UserQuery userQuery) {
        System.out.println("Person query");
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(String.format(" SELECT DISTINCT ?person ?%s ?comment WHERE {", userQuery.get("property")));
        sb.append(String.format("  ?person a dbo:Person ."));
        sb.append("?person foaf:name ?name .");
        sb.append(String.format("  ?name <bif:contains> \"'%s'\" .", userQuery.get("value")));
        sb.append("  ?person rdfs:comment ?comment .");
        // 'of' property e.g. influencedBy
        if (predicates.contains(userQuery.get("property"))) {
            sb.append(String.format("  ?%s %s ?person .", userQuery.get("property"), userQuery.get("iri")));
        } else {
            sb.append(String.format("  ?person %s ?%s .", userQuery.get("iri"), userQuery.get("property")));
        }
        sb.append(" FILTER  langMatches(lang(?comment), 'en')");
        sb.append(String.format("} LIMIT %s", (userQuery.get("rlimit") == "") ? "1" : userQuery.get("rlimit")));

        System.out.println(sb.toString());

        return sb.toString();
    }

    public static String generateCountryQuery(UserQuery userQuery) {
        System.out.println("Country query");
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX dbr: <http://dbpedia.org/resource/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append("SELECT * WHERE {\n");
        sb.append(String.format("%s %s ?%s\n",userQuery.get("value"),userQuery.get("iri"),userQuery.get("property")));
        if (userQuery.get("property").equalsIgnoreCase("comment"))
            sb.append(String.format("FILTER  langMatches(lang(?%s), 'en')\n", userQuery.get("property")));
        sb.append("}");
        return sb.toString();
    }

    public static String generateListQuery(UserQuery userQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
        sb.append(" PREFIX vrank:<http://purl.org/voc/vrank#>");
        sb.append(" SELECT ?person ?name ?v");
        sb.append(" FROM <http://dbpedia.org>");
        sb.append(" FROM <http://people.aifb.kit.edu/ath/#DBpedia_PageRank>");
        sb.append(" WHERE {");
        sb.append("   {");
        sb.append("     SELECT ?person (SAMPLE(?names) as ?name) WHERE {");
        sb.append(String.format(" ?person a %s . ?person foaf:name ?names .", userQuery.get("iri")));
        sb.append(" FILTER (langMatches(lang(?names), \"EN\"))");
        sb.append("     } GROUP BY ?person");
        sb.append("   }");
        sb.append("   OPTIONAL { ?person vrank:hasRank/vrank:rankValue ?v }");
        sb.append(" }");
        sb.append(" ORDER BY DESC(?v)");
        sb.append(" LIMIT 10");

        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String generateListConditionalQuery(UserQuery userQuery) {
        System.out.println("List conditional query");
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
        sb.append(" PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>");
        sb.append(" PREFIX vrank:<http://purl.org/voc/vrank#>");
        sb.append(" SELECT  ?person ?name ?date");
        sb.append(" FROM <http://dbpedia.org>");
        sb.append(" FROM <http://people.aifb.kit.edu/ath/#DBpedia_PageRank>");
        sb.append(" WHERE {");
        sb.append("   {");
        sb.append("     SELECT ?person (SAMPLE(?names) as ?name) WHERE {");
        sb.append(String.format(" ?person a %s . ?person foaf:name ?names .", userQuery.get("iri")));
        if (userQuery.get("condition_property").equals("foaf:name")) { // filter by name
            sb.append(String.format(" ?names <bif:contains> \"'%s'\"", userQuery.get("condition_value")));
        }
        sb.append(" FILTER (langMatches(lang(?names), \"EN\"))");
        sb.append("     } GROUP BY ?person");
        sb.append("   }");
        sb.append("   ?person vrank:hasRank/vrank:rankValue ?v .");
        if (userQuery.get("condition_property_type").equals("date")) {
            int year = Integer.parseInt(userQuery.get("condition_value"));
            sb.append("   ?person dbo:birthDate ?date .");
            if (userQuery.get("condition_operator").equals("LESS_THAN")) {
                sb.append(String.format("   FILTER (?date < xsd:date(\"%s-01-01\"))",year));
            } else if (userQuery.get("condition_operator").equals("GREATER_THAN")) {
                sb.append(String.format("   FILTER (?date >= xsd:date(\"%s-01-01\"))",year));
            } else {
                // equal to
                sb.append(String.format("   FILTER (?date >= xsd:date(\"%s-01-01\") && ?date < xsd:date(\"%s-01-01\"))",year,year+1));
            }
        }
        sb.append(" }");
        sb.append(" ORDER BY DESC(?v)");
        sb.append(" LIMIT 10");

        System.out.println(sb.toString());
        return sb.toString();
    }
}
