package com3001.at00672.model;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.HashMap;
import java.util.Map;

public class DBpediaResource {
    String uri;
    Map<Resource, RDFNode> properties = new HashMap<>();
    public DBpediaResource() {
    }

    public DBpediaResource(Resource property, RDFNode value) {
        properties.put(property, value);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
