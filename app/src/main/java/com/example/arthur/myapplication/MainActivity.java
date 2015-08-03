package com.example.arthur.myapplication;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.myText);
    }

    public void newPost(View view) {
        Intent intent = new Intent(this, PostHttpActivity.class);
        startActivity(intent);
    }

    public void checkItUrl( View view ) throws IOException {
        client.get("http://192.168.0.10:3030", new JsonHttpResponseHandler() {

            // success, single json object. this is very unlikely
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }

            // success, json array. this will pretty much always be the case
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray articles) {
                try {
                    jsonToArticles(articles);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "fail: onSuccess: JSONException: jsonToArticles", Toast.LENGTH_SHORT).show();
                    textView.setText(
                            e.getLocalizedMessage() + "\n" +
                                    e.getMessage() + "\n" +
                                    e.getCause()
                    );
                }
            }
        });
    }

    public void jsonToArticles( JSONArray jsons ) throws JSONException {
        ArrayList< Article > articles = new ArrayList<>();
        String those = "";
        for( int i = 0; i < jsons.length(); i++ ) {
            articles.add( readOneArticle( (JSONObject) jsons.get( i )));
        }
        textView.setText( articles.toString() );
    }

    public Article readOneArticle( JSONObject article ) throws JSONException {
        return new Article(
            article.get( "title" ).toString(),
            article.get( "body" ).toString(),
            article.get( "link" ).toString() );
    }
}
