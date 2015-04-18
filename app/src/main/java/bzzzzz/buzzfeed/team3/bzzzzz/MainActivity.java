package bzzzzz.buzzfeed.team3.bzzzzz;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private static String url = "http://www.buzzfeed.com/buzzfeed/api/buzzes?ids=1234,234,4567&session_key=2ee4e2768c44aedd9481f2e70855fb6716323a5c8995da8a7386fd863aee54bfhackathon2";
    private ConnectivityManager connMgr;
//    TextView articleName;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //custom font
        SpannableString s = new SpannableString(" BzZzZz");
        s.setSpan(new TypefaceSpan(this, "ProximaNova-Semibold.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

        // call AsyncTask to perform network operation on separate thread
        new HttpAsyncTask().execute(url);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //articleName.setText(result);

            String user;
            String uri;
            String article_url;

            //convert json to result
            try {
                JSONObject jsonResult = new JSONObject(result);

                JSONArray buzzes = jsonResult.getJSONArray("buzzes"); // get buzzes array
                JSONObject buzz = buzzes.getJSONObject(0); // get first article in the array

                //buzzes.getJSONObject(0).names() // get first article keys [title,url,categories,tags]
                user = buzz.getString("username");
                uri = buzz.getString("uri");

                article_url = "http://www.buzzfeed.com/" + user + "/" + uri;
                //articleName.setText(article_url);

//                Intent i = new Intent(getBaseContext(), WebviewActivity.class);
//                i.putExtra("article", article_url);
//                startActivity(i);

                webView = (WebView) findViewById(R.id.webView1);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(article_url);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}



















