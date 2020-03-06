package com3001.at00672.service;

import com3001.at00672.model.UserQuery;

import static org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf.iri;

public class QueryBuilder {
    public static String generateQuery(UserQuery userQuery) {
        String result = "";
        switch (userQuery.get("function")) {
            case "query": result = generatePersonQuery(userQuery); break;
            case "list": result = generateListQuery(userQuery); break;
            case "list_conditional": result = generateListConditionalQuery(userQuery); break;
            default: result = "";
        }
        return result;

    }

    public static String generatePersonQuery(UserQuery userQuery) {
        System.out.println("Person query");
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(String.format(" SELECT DISTINCT ?r ?%s ?comment WHERE {", userQuery.get("property")));
        sb.append(String.format("  ?r foaf:name ?name; a dbo:%s .", userQuery.get("topic")));
        sb.append(String.format("  ?name <bif:contains> \"'%s'\" .", userQuery.get("value")));
        sb.append(String.format("  ?%s rdfs:comment ?comment .", userQuery.get("topic")));
        sb.append(String.format("  ?%s %s ?%s .", userQuery.get("topic"), userQuery.get("iri"), userQuery.get("property")));
        sb.append(" FILTER  langMatches(lang(?comment), 'en')");
        sb.append(String.format("} LIMIT %s", (userQuery.get("rlimit") == "") ? "1" : userQuery.get("rlimit")));

        System.out.println(sb.toString());

        return sb.toString();
    }

    public static String generateListQuery(UserQuery userQuery) {
        // TODO: FIX me
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


        /*

    //TODO: Come back to trying to use rdf4j sparqlbuilder

    protected static final Prefix dbo   =  SparqlBuilder.prefix("dbo", iri("http://dbpedia.org/ontology/"));
    protected static final Prefix prop   =  SparqlBuilder.prefix("prop", iri("http://dbpedia.org/property/"));
    protected static final Prefix foaf   =  SparqlBuilder.prefix("foaf", iri("http://xmlns.com/foaf/0.1/"));
    protected static final Prefix rdfs  =  SparqlBuilder.prefix("rdfs", iri("http://www.w3.org/2000/01/rdf-schema#"));
    protected static final Iri bifContains = iri("bif:contains");
    protected static SelectQuery selectQuery;
    public static void main(String[] args) {
        SelectQuery("Person", "comment", "alex");
    }


    public static String GenerateQuery(String topic, String query, String value) {
        switch(topic) {
            case "person": return PersonQuery(query, value);
            default: return null;
        }
    }


    public static String SelectQuery(String topicString, String propertyString, String valueString) {
        selectQuery = Queries.SELECT();
        Variable topic = SparqlBuilder.var(topicString);
        Variable property =  SparqlBuilder.var(propertyString);
        Variable value = SparqlBuilder.var(valueString);
        Variable label = SparqlBuilder.var("label");
        TriplePattern p1 = GraphPatterns.tp(topic, rdfs.iri("label"), label)
                                        .andIsA(dbo.iri((topicString)));
        TriplePattern p2 = label.has(bifContains, valueString);
        //Expression<?> concat = Expressions.cu(Rdf.literalOf("langMatches(lang(?comment)"),Rdf.literalOf("'en'"));
        GraphPattern optionalInner1 = GraphPatterns.tp(topic, rdfs.iri(propertyString), property);
        //TriplePattern optionalFilter = GraphPatterns.tp(SparqlBuilder.var("FILTER"), iri(""), Rdf.literalOf("langMatches(lang(?comment), 'en'"));
        GraphPatternNotTriples optional = GraphPatterns.optional(optionalInner1,GraphPatterns.tp(new RdfBlankNode.PropertiesBlankNode()));
        selectQuery.prefix(dbo,prop,foaf,rdfs)
            .select(property)
            .where(p1,p2, optional);

        /*

        SELECT ?comment WHERE {
          ?person rdfs:label ?label; a dbo:Person .
          ?label <bif:contains> "'alan turing'".
          ?uri rdfs:label ?txt .
          OPTIONAL { ?person rdfs:comment ?comment .
             FILTER  langMatches(lang(?comment), 'en') }
        } LIMIT 1
        */
}
