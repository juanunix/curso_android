package com.paola.httpapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkActivity extends AppCompatActivity {

    Button button;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_network);
        button = (Button)findViewById(R.id.button);
    //    webView = (WebView) findViewById(R.id.webView);

       // webView.getSettings().setAllowContentAccess(true);


    }

    public void download(View view){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTask().execute(button.getText().toString());
        } else {
            Log.e(NetworkActivity.class.getSimpleName(),  "No hay red disponible. Trate de habilitar la red para poder conectarse a internet");
        }


    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader= null;
            String downloadResult=null;

            try {

                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();


                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    StringBuffer buffer = new StringBuffer();

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                    }

                    if (buffer.length() != 0) {
                        downloadResult = buffer.toString();
                    }
                }
            }catch(Exception e){
                Log.e(NetworkActivity.class.getSimpleName(), "Error ", e);
            }
            finally {
                if(urlConnection !=null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(NetworkActivity.class.getSimpleName(), "Error closing stream", e);
                    }
                }
            }
            return downloadResult;
        }


        @Override
        protected void onPostExecute(String htmlString) {
            if(htmlString!=null) Log.i(NetworkActivity.class.getSimpleName(),htmlString);

         //  webView.loadData(htmlString,"text/html; charset=utf-8", null);
         //   webView.loadUrl("https://android.com");

            Uri uri = Uri.parse("http://android.com");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_network, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
