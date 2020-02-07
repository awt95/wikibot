package com3001.at00672;
import org.apache.jena.base.Sys;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagRemoverTest {
    public static void main(String[] args) {
        String text = "Let me find out who Alan Turing is. <oob><query type=\"Person\">\"'Alan Turing'\"</query></oob>";
        Pattern pattern = Pattern.compile("<oob>(.*)</oob>");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String innerContent = matcher.group(1);
            System.out.println(innerContent);
            Pattern patternInner = Pattern.compile("<query type=(.*)>(.*)</query>");
            Matcher matcherInner = patternInner.matcher(innerContent);
            if (matcherInner.find()) {
                System.out.println("Group count: " + matcherInner.groupCount());
                String queryType = matcherInner.group(1);
                String queryContent = matcherInner.group(2);
                System.out.println("type: " + queryType);
                System.out.println("content: " + queryContent);
            }
        }

    }
}
