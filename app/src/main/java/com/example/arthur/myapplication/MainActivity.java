package com.example.arthur.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "HttpExample";
//    private EditText urlText;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        urlText = (EditText) findViewById(R.id.myUrl);
        textView = (TextView) findViewById(R.id.myText);
    }

    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(View view) {
        // Gets the URL from the UI's text field.
//        String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute("nothing");
        } else {
            textView.setText("No network connection available.");
        }
    }

    public void newPost( View view ) {

    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, List< Article >> {
        @Override
        protected List< Article > doInBackground(String... urls) {

            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return new ArrayList< Article>();
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(List< Article > articles ) {
            String result = "";
            for( Article elem : articles ) {
                result += "title: " + elem.title + ", body: " + elem.body + ";\n";
            }
            textView.setText(result);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private List< Article > downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL("http://192.168.0.10:3030");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
//            return readIt( is, len );
            return readJsonStream( is );

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /*
     *
     *      oh my laundry this is so much what is any of this does it mean nothing
     *
     */


    public List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        return readMessagesArray(reader);
    }

    public List readMessagesArray(JsonReader reader) throws IOException, UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
        List messages = new ArrayList<Article>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Article readMessage(JsonReader reader) throws IOException {
        String title = "";
        String body = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("body")) {
                body = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Article(title, body);
    }

//    public List readDoublesArray(JsonReader reader) throws IOException {
//        List doubles = new ArrayList();
//
//        reader.beginArray();
//        while (reader.hasNext()) {
//            doubles.add(reader.nextDouble());
//        }
//        reader.endArray();
//        return doubles;
//    }

//    public Article readUser(JsonReader reader) throws IOException {
//        String username = null;
//        String followersCount = null;
//
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String name = reader.nextName();
//            if (name.equals("title")) {
//                username = reader.nextString();
//            } else if (name.equals("followers_count")) {
//                followersCount = reader.nextString();
//            } else {
//                reader.skipValue();
//            }
//        }
//        reader.endObject();
//        return new Article(username, followersCount);
//    }

    private class Article {
        public String title;
        public String body;

        public Article( String t, String b ) {
            title = t;
            body = b;
        }
    }

}
