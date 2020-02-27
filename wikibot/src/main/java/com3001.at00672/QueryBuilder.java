package com3001.at00672;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expression;
import org.eclipse.rdf4j.sparqlbuilder.constraint.Expressions;
import org.eclipse.rdf4j.sparqlbuilder.core.Prefix;
import org.eclipse.rdf4j.sparqlbuilder.core.QueryElementCollection;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPattern;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatternNotTriples;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.TriplePattern;
import org.eclipse.rdf4j.sparqlbuilder.rdf.*;

import static org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf.iri;

public class QueryBuilder {
    public static void main(String[] args) {
        String topic = "Person";
        String iri = "dbo:birthPlace";
        String namespace = "dbo";
        String property = "birthPlace";
        String value = "Alan Turing";
        UserQuery userQuery = new UserQuery(topic, iri, value, namespace, property);
        String dbQuery = generateQuery(userQuery);
        userQuery.setQueryString(dbQuery);
        System.out.println(dbQuery);
        String serverResponse = DBPedia.executeQuery(userQuery);
        System.out.println(serverResponse);
        //System.out.println(DBPedia.executeQuery(query, property, propertyName));
    }
    public static String generateQuery(UserQuery userQuery) {
        String result = "";
        switch (userQuery.getFunction()) {
            case "query": result = generatePersonQuery(userQuery); break;
            case "list": result = generateListQuery(userQuery); break;
            default: result = "Sorry, I don't know";
        }
        return result;

    }

    public static String generatePersonQuery(UserQuery userQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(String.format(" SELECT DISTINCT ?%s ?comment WHERE {", userQuery.getProperty()));
        sb.append(String.format("  ?%s foaf:name ?name; a dbo:%s .", userQuery.getTopic(), userQuery.getTopic()));
        sb.append(String.format("  ?name <bif:contains> \"'%s'\" .", userQuery.getValue()));
        sb.append(String.format("  ?%s rdfs:comment ?comment .", userQuery.getTopic()));
        sb.append(String.format("  ?%s %s ?%s .", userQuery.getTopic(), userQuery.getIri(), userQuery.getProperty()));
        sb.append(" FILTER  langMatches(lang(?comment), 'en')");
        sb.append("}");
        if (userQuery.getProperty().equals("birthPlace")) {
            // TODO: expand to other queries
        } else {
            sb.append(" LIMIT 1");
        }

        System.out.println(sb.toString());

        return sb.toString();
    }

    public static String generateListQuery(UserQuery userQuery) {
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(" SELECT ?name ?person WHERE {");
        sb.append(String.format("  ?person a %s .\n", userQuery.getIri()));
        sb.append("?person foaf:name ?name .");
        sb.append("?person rdfs:comment ?comment .");
        sb.append(" FILTER  langMatches(lang(?comment), 'en') .");
        sb.append(" FILTER (REGEX(?name, \"^[A-Z]\", \"i\"))\n");
        sb.append(" } ORDER BY ?name LIMIT 100");

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
