package gacglc.app;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GareListFragment.GareListListener, OwnIconRendered.OIRInterface {

    private static final String TAG = MainActivity.class.getSimpleName();

    private gacglc.app.List list = new List();
    FragmentManager fragmentManager;
    private Button mapBtn;
    private Button mainBtn;
    private Button detailsBtn;
    private FloatingActionButton refresh;
    private ProgressBar pgbLoading;
    public static int VALUE=1;

    private RequestQueue requestQueue;

    Dao<Gare, String> gareDao;
    public ArrayList<Gare> garesDb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        gareDao = dbHelper.getDataDao();

        requestQueue = Volley.newRequestQueue(MainActivity.this);


        final Intent mapsIntent = new Intent(this, MapsActivity.class);
        mapsIntent.putParcelableArrayListExtra("GARES_LIST", garesDb);
        mapBtn = (Button) findViewById(R.id.button2);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(mapsIntent, VALUE);
            }
        });

        final Intent mainIntent = new Intent(this, MainActivity.class);
        mainBtn = (Button) findViewById(R.id.button3);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayGareList();
            }
        });

        detailsBtn = (Button) findViewById(R.id.button4);
        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDetails();
            }
        });

        refresh = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualise();
            }
        });

        pgbLoading = (ProgressBar) findViewById(R.id.a_main_pgb_loading);

        fragmentManager = getSupportFragmentManager();
        pgbLoading.setVisibility(View.INVISIBLE);

        // Get info about gares
        getMyList("https://data.sncf.com/api/records/1.0/search/?dataset=referentiel-gares-voyageurs&sort=intitule_gare&rows=3500");
    }

    public void disable() {
        detailsBtn.setEnabled(false);
        mainBtn.setEnabled(false);
        mapBtn.setEnabled(false);
        refresh.setEnabled(false);
    }

    public void enable() {
        detailsBtn.setEnabled(true);
        mainBtn.setEnabled(true);
        mapBtn.setEnabled(true);
        refresh.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {

        if (requestCode == VALUE) {
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    runBis(resultCode);
                }
                public void runBis(int resultCode) {
                    gacglc.app.GareInfoFragment gareInfoFragment = gacglc.app.GareInfoFragment.newInstance(garesDb.get(resultCode));
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.a_main_lyt_fragment_container, gareInfoFragment);
                    transaction.commit();
                }
            });
        }
    }

    public void displayGareList() {
        refresh.setVisibility(View.VISIBLE);
        // Create a fragment using factory method
        gacglc.app.GareListFragment gareListFragment = gacglc.app.GareListFragment.newInstance(getGares());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.a_main_lyt_fragment_container, gareListFragment);
        transaction.commit();
    }

    public void displayInfo(Gare gare) {
        refresh.setVisibility(View.INVISIBLE);
        // Create a fragment using factory method
        gacglc.app.GareInfoFragment gareInfoFragment = gacglc.app.GareInfoFragment.newInstance(gare);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.a_main_lyt_fragment_container, gareInfoFragment);
        transaction.commit();
    }

    public void displayDetails() {
        refresh.setVisibility(View.INVISIBLE);
        // Create a fragment using factory method
        gacglc.app.DetailsFragment detailsFragment = gacglc.app.DetailsFragment.newInstance();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.a_main_lyt_fragment_container, detailsFragment);
        transaction.commit();
    }

    public ArrayList<Gare> getGares() {
        garesDb.clear();
        try {
            garesDb.addAll(gareDao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return garesDb;
    }

    private void getMyList(String name) {
        cleanDao();
        pgbLoading.setVisibility(View.VISIBLE);
        disable();
        String url = name;
        if (!readFromFile(getApplicationContext()).equals("OK")) {
            StringRequest getGareRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new GsonBuilder().create();
                            list = gson.fromJson(response, gacglc.app.List.class);
                            ArrayList<Fields> truc = list.getRecords();
                            for (Fields machin : truc)
                                garesDb.add(machin.getFields());

                            try {
                                for (int i = 0; i < garesDb.size(); i++)
                                    gareDao.createOrUpdate(garesDb.get(i));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            // Hide progress bar
                            pgbLoading.setVisibility(View.INVISIBLE);
                            // Display gare list fragment
                            displayGareList();
                            enable();

                        }
                    },
                    simpleErrorListener);
            getGareRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(getGareRequest);
            writeToFile("OK", getApplicationContext());
        }
        else {
            // Hide progress bar
            pgbLoading.setVisibility(View.INVISIBLE);
            // Display gare list fragment
            displayGareList();
            enable();
        }

    }

    public void actualise() {
        writeToFile("",getApplicationContext());
        getMyList("https://data.sncf.com/api/records/1.0/search/?dataset=referentiel-gares-voyageurs&sort=intitule_gare&rows=3500");
    }

    private void cleanDao() {

    }
    /**
     * Simple error listener that just log error in logcat
     */
    private Response.ErrorListener simpleErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Log.e(TAG, error.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Non actualisé", Toast.LENGTH_SHORT).show();
            displayGareList();
            pgbLoading.setVisibility(View.INVISIBLE);
        }
    };

    public void toaster() {
        Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        displayGareList();
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "NO";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.replace(0,2,receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
