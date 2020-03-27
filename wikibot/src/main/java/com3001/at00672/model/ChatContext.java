package com3001.at00672.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChatContext {

    private String subject;
    private String subjectURI;
    private Map<String, String> results;
    private UserQuery currentQuery; // Implement here?
    private List<UserQuery> queryHistory; // ??
    private String botResponse; // TODO: Create object for?

    public ChatContext() {
        results = new LinkedHashMap<>();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSubject(String subject, String subjectURI) {
        this.subject = subject;
        this.subjectURI = subjectURI;
    }

    public String getSubjectURI() {
        return subjectURI;
    }

    public void setSubjectURI(String subjectURI) {
        this.subjectURI = subjectURI;
    }

    public String getBotResponse() {
        return botResponse;
    }

    public void setBotResponse(String botResponse) {
        this.botResponse = botResponse;
    }

    public Map<String, String> getResults() {
        return results;
    }

    public void add(String k, String v) {
        results.put(k,v);
    }

    public void setResults(Map<String, String> results) {
        this.results = results;
    }

    public void clear() {
        clearSubject();
        clearResults();
    }

    public void clearSubject() {
        subject = "";
        subjectURI = "";
    }

    public void clearResults() {
        results.clear();
    }
}
