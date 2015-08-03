package com.example.arthur.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PostHttpActivity extends AppCompatActivity {

    TextView newTitle, newBody, newLink;
    AsyncHttpClient client = new AsyncHttpClient();

    public void submitNewPost( View view ) throws IOException {
        createArticle( newTitle.getText().toString(), newBody.getText().toString(), newLink.getText().toString() );
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createArticle( String title, String body, String link ) {
        RequestParams params = new RequestParams();
        params.put( "title", title );
        params.put( "body", body );
        params.put("link", link);

        client.post("http://192.168.0.10:3030/newArticle", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String decoded = new String(response, "UTF-8");
                    Toast.makeText(getApplicationContext(), "Successful Addition to the Database", Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(), "whups", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_http);

        newTitle = (TextView) findViewById( R.id.new_post_title );
        newBody =  (TextView) findViewById( R.id.new_post_body );
        newLink =  (TextView) findViewById( R.id.new_post_link );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_http, menu);
        return true;
    }
}