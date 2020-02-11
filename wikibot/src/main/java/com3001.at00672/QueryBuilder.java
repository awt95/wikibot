package com3001.at00672;

public class QueryBuilder {

    public static String GenerateQuery(String topic, String query, String value) {
        switch(topic) {
            case "person": return PersonQuery(query, value);
            default: return null;
        }
    }


    public static String PersonQuery(String query, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(" PREFIX dbo: <http://dbpedia.org/ontology/>");
        sb.append(" PREFIX prop: <http://dbpedia.org/property/>");
        sb.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>");
        sb.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sb.append(" SELECT ?comment WHERE {");
        sb.append("  ?person rdfs:label ?label; a dbo:Person .");
        sb.append("  ?label <bif:contains> \"'" + value +"'\".");
        sb.append("  ?uri rdfs:label ?txt .");
        sb.append("  OPTIONAL { ?person rdfs:comment ?comment . ");
        sb.append("    FILTER  langMatches(lang(?comment), 'en') }");
        sb.append("} LIMIT 1");
        return sb.toString();
    }
}
