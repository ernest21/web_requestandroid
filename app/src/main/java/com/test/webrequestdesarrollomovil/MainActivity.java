package com.test.webrequestdesarrollomovil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.database.Cursor;

public class MainActivity extends ActionBarActivity {


    TextView tvIsConnected;
    ListView lvResponse;
    DatabaseHelper db;
    Cursor movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DatabaseHelper(getApplicationContext());
        // get reference to the views
        tvIsConnected = (TextView) findViewById(R.id.textView);
        lvResponse = (ListView)findViewById(R.id.listView);

         movies = db.getAll("peliculas");
        // check if you are connected or not
        if(isConnected()){
            try {
                  new HttpAsyncTask().execute("http://www.omdbapi.com/?s=Batman&y=&plot=short&type=movie&r=json");
                  tvIsConnected.setText("You are connected");
                  tvIsConnected.setTextColor(Color.GREEN);

            }catch (Exception e){
                tvIsConnected.setText("You are NOT connected");
                tvIsConnected.setTextColor(Color.RED);

            }
        } else if (movies.getCount() != 0){
            tvIsConnected.setText("You are NOT connected");
            tvIsConnected.setTextColor(Color.RED);
            show(movies);

        } else {
            tvIsConnected.setText("No data and conection");
            tvIsConnected.setTextColor(Color.RED);


        }

        lvResponse.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {

                Intent detailView = new Intent(getApplicationContext(), DetailActivity.class);
                detailView.putExtra("item_id", view.getTag().toString());
                startActivity(detailView);

            }

        });

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;

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

            String jsonData = result;

            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();

            JSONObject j;
            MovieOMDB movieOMDBClass = null;

            try {
                //Deserialize the json to the specified class
                j = new JSONObject(jsonData);
                movieOMDBClass = gson.fromJson(j.toString(), MovieOMDB.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Search [] movieOMDBArray = movieOMDBClass.getSearch().toArray(new Search[movieOMDBClass.getSearch().size()]);

            //Create a custom adapter to display the info in the list view

            try {
                ArrayList<Search> tmp = new ArrayList<Search>(movieOMDBClass.getSearch());
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), tmp);
                if (movies.getCount() == 0) {
                    for (int i = 0; i <= tmp.size(); i++) {
                        db.insertTable("peliculas", "titulo", tmp.get(i).getTitle().toString());
                        db.insertTable("peliculas", "ano", tmp.get(i).getYear().toString());
                        db.insertTable("peliculas", "imdb", tmp.get(i).getImdbID().toString());
                        db.insertTable("peliculas", "tipo", tmp.get(i).getType().toString());
                        db.insertTable("peliculas","titulo",tmp.get(i).getTitle().toString(),"ano",tmp.get(i).getYear().toString(),"imdb",tmp.get(i).getImdbID().toString(),"tipo",tmp.get(i).getType());
                    }
                }
                lvResponse.setAdapter(adapter);

            }catch (Exception e){
                showMessage("No data","Connect to intenet");
                }
            }

        }

    public void show(Cursor c) {

        ListView list = (ListView) findViewById(R.id.listView);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[]{"titulo"}, new int[]{android.R.id.text1});

        if (c.getCount() == 0) {
            showMessage("Error", "No records found");
            return;
        } else {
            list.setAdapter(cursorAdapter);
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

