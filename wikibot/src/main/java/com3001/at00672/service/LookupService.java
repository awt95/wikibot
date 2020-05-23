/**
 * LookupService
 * - Currently not in use
 * - Proof of concept for future work connection with DBPedia Search
 * @author Alex Turner
 */
package com3001.at00672.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.net.URL;
import java.util.Iterator;

public class LookupService {
    public static void main(String[] args) {
        try {
            String urlString = "http://lookup.dbpedia.org/api/search/PrefixSearch?";
            urlString += "QueryClass=person";
            urlString += "&QueryString=alan";
            System.out.println(urlString);
            URL url = new URL(urlString);

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(urlString);
            httpGet.addHeader("Accept", "application/json");
            HttpResponse response = client.execute(httpGet);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sax = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler();
            sax.parse(url.openStream(), handler);
            System.out.println(handler.toString());
            if(response.getStatusLine().getStatusCode() >= 400) {
                System.out.println("Handler error : " + response.getStatusLine());
            }
            else {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode root = new ObjectMapper().readTree(json).get("results");
                Iterator<JsonNode> it = root.iterator();
                while (it.hasNext()) {
                    JsonNode thisNode = it.next();
                    System.out.println(thisNode);
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
