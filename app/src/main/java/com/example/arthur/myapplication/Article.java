package com.example.arthur.myapplication;

/**
 * Created by arthur on 8/1/15.
 */

public class Article {
    public String title;
    public String body;
    public String link;

    public Article( String t, String b, String l) {
        title = t;
        body = b;
        link = l;
    }

    public String toString() {
        return "article:\n"     +
                "title: "       + title     + "\n" +
                "body: "        + body      + "\n" +
                "link: "        + link      + "\n\n";
    }
}
