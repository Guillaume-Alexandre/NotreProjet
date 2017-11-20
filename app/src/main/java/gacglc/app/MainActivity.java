package gacglc.app;

import android.app.DownloadManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private gacglc.app.List list = new List();
    private FragmentManager fragmentManager;
    private FloatingActionButton addGareBtn;
    private ProgressBar pgbLoading;

    private RequestQueue requestQueue;

    Dao<Gare, String> gareDao;
    ArrayList<Gare> garesDb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        gareDao = dbHelper.getDataDao();

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        addGareBtn = (FloatingActionButton) findViewById(R.id.a_main_btn_add_bottle);
        addGareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pgbLoading = (ProgressBar) findViewById(R.id.a_main_pgb_loading);

        fragmentManager = getSupportFragmentManager();

        // Get info about gares
        //getMyList("https://chivas-container.herokuapp.com/cellars/Gaetan");
        getMyList("https://data.sncf.com/api/records/1.0/search/?dataset=referentiel-gares-voyageurs&sort=intitule_gare&rows=10");
    }

    private void displayGareList() {
        // Show "+" button
        addGareBtn.setVisibility(View.VISIBLE);

        // Create a fragment using factory method
        gacglc.app.GareListFragment gareListFragment = gacglc.app.GareListFragment.newInstance(getGares());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.a_main_lyt_fragment_container, gareListFragment);
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
        String url = name;
        StringRequest getGareRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().create();
                        list = gson.fromJson(response, gacglc.app.List.class);
                        ArrayList<Fields> truc = list.getRecords();
                        for(Fields machin:truc)
                            garesDb.add(machin.getFields());

                        try {
                            for (int i=0; i<garesDb.size(); i++)
                                gareDao.createOrUpdate(garesDb.get(i));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // Hide progress bar
                        pgbLoading.setVisibility(View.GONE);
                        // Display gare list fragment
                        displayGareList();
                    }
                },
                simpleErrorListener);

        requestQueue.add(getGareRequest);
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
            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_SHORT).show();
        }
    };
}
