package com3001.at00672.model;

import org.alicebot.ab.Predicates;

import java.util.*;
import java.util.stream.Collectors;

public class UserQuery {
    // TODO: Refactor to map for conciseness

    private Map<String, String> predicates = new HashMap<>();
    private String queryString;


    public UserQuery(Predicates predicates) {
        this.predicates.putAll(predicates);
    }

    public UserQuery() {

    }

    public String get(String key) {
        return (predicates.containsKey(key)) ? predicates.get(key) : "";
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String toString() {
        Iterator it = predicates.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            sb.append(pair.getKey() + " = " + pair.getValue() + "\n");
        }
        return sb.toString();
    }
}
