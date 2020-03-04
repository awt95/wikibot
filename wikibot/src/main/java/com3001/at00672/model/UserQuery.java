package com3001.at00672.model;

import org.alicebot.ab.Predicates;

import java.util.HashMap;

public class UserQuery {
    private String topic;
    private String iri;
    private String value;
    private String namespace;
    private String property;
    private HashMap<String,String> queryProperties = new HashMap<>();
    private String queryString;
    private String function;
    private Predicates predicates;

    public UserQuery() {}


    public UserQuery(String topic,  String iri, String value, String namespace, String property) {
        this.topic = topic;
        this.iri = iri;
        this.value = value;
        this.namespace = namespace;
        this.property = property;

    }

    private void SanitizeAttributes() {
        topic = topic.replaceAll("[^a-zA-Z0-9]", "");
        value = value.replaceAll("[^a-zA-Z0-9]", "");
        property = property.replaceAll("[^a-zA-Z0-9]", "");
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Topic: " + topic + "\r\n");
        sb.append("IRI: " + iri + "\r\n");
        sb.append("Namespace: " + namespace + "\r\n");
        sb.append("Property: " + property + "\r\n");
        sb.append("Value: " + value + "\r\n");
        return sb.toString();
    }

    public HashMap getQueryProperties() {
        return queryProperties;
    }

    public void addQueryProperty(String key, String value) {
        queryProperties.put(key, value);
    }

    public String getQueryProperty(String key) {
        return queryProperties.get(key);
    }

    public Predicates getPredicates() {
        return predicates;
    }

    public void setPredicates(Predicates predicates) {
        this.predicates = predicates;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
